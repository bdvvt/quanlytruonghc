package com.example.quanlytruonghoc.models.services.impl;

import com.example.quanlytruonghoc.exceptions.AuthException;
import com.example.quanlytruonghoc.exceptions.NotFoundException;
import com.example.quanlytruonghoc.models.data.dto.constants.RoleName;
import com.example.quanlytruonghoc.models.data.dto.constants.UserStatus;
import com.example.quanlytruonghoc.models.data.entities.Role;
import com.example.quanlytruonghoc.models.data.entities.User;
import com.example.quanlytruonghoc.models.data.req.auth.ActiveUserReq;
import com.example.quanlytruonghoc.models.data.req.auth.LoginReq;
import com.example.quanlytruonghoc.models.data.req.auth.RegisterReq;
import com.example.quanlytruonghoc.models.data.res.LoginRes;
import com.example.quanlytruonghoc.models.data.mapper.UserMapper;
import com.example.quanlytruonghoc.models.repositories.IRoleRepository;
import com.example.quanlytruonghoc.models.repositories.IUserRepository;
import com.example.quanlytruonghoc.models.services.IAuthService;
import com.example.quanlytruonghoc.models.services.IMailService;
import com.example.quanlytruonghoc.security.jwt.JWTUtils;
import com.example.quanlytruonghoc.security.jwt.JwtTokenFilter;
import com.example.quanlytruonghoc.security.jwt.TokenBlacklistService;
import com.example.quanlytruonghoc.security.principal.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager manager;
    private final JWTUtils jwtUtils;
    private final IMailService mailService;
    private final JwtTokenFilter jwtTokenFilter;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserMapper userMapper;

    @Override
    public LoginRes login(LoginReq req) {
        Authentication authentication;
        try {
            authentication = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsernameOrEmail(), req.getPassword())
            );
        } catch (DisabledException e) {
            throw new AuthException("Vui lòng active tài khoản trước khi đăng nhập !");
        } catch (LockedException e) {
            throw new AuthException("Tài khoản của bạn đã bị khóa");
        } catch (AuthenticationException e) {
            throw new AuthException("Mật khẩu hoặc tài khoản không đúng");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return LoginRes.builder()
                .accessToken(jwtUtils.generateToken(userDetails.getUsername()))
                .roles(
                        userDetails.getUser().getRoles()
                                .stream().map(role -> role.getRoleName().name())
                                .collect(Collectors.toSet())
                )
                .build();
    }

    @Override
    public void register(RegisterReq req) {
        Set<Role> roles = new HashSet<>();
        roles.add(
                roleRepository.findByRoleName(RoleName.STUDENT)
                        .orElseThrow(() -> new NotFoundException("Role not found"))
        );
        String otp = String.valueOf((int) ((Math.random() * 900000) + 100000));
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(1);
        User user = userMapper.toEntity(req);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRoles(roles);
        user.setStatus(UserStatus.INACTIVE);
        user.setOtpCode(otp);
        user.setOtpExpiration(expiration);
        userRepository.save(user);
        mailService.sendOtpMail(user.getEmail(), otp);
    }

    @Override
    public void logout(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            tokenBlacklistService.blacklistToken(token);
        }
        SecurityContextHolder.clearContext();
    }

    @Override
    public String activeUser(ActiveUserReq req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getStatus() == UserStatus.ACTIVE) {
            return "Tài khoản đã được kích hoạt trước đó.";
        }

        if (user.getOtpExpiration() == null || user.getOtpExpiration().isBefore(LocalDateTime.now())) {
            String newOtp = String.valueOf((int) ((Math.random() * 900000) + 100000));
            LocalDateTime newExpiration = LocalDateTime.now().plusMinutes(1);
            user.setOtpCode(newOtp);
            user.setOtpExpiration(newExpiration);
            userRepository.save(user);
            mailService.sendOtpMail(user.getEmail(), newOtp);

            throw new RuntimeException("Mã OTP đã hết hạn! Hệ thống đã tự động gửi mã OTP mới về email của bạn.");
        }

        if (user.getOtpCode() == null || !user.getOtpCode().equals(req.getOtp())) {
            throw new RuntimeException("Mã OTP không chính xác");
        }
        user.setStatus(UserStatus.ACTIVE);
        user.setOtpCode(null);
        user.setOtpExpiration(null);
        userRepository.save(user);

        return "Kích hoạt tài khoản thành công! Bạn có thể đăng nhập ngay bây giờ.";
    }





}
