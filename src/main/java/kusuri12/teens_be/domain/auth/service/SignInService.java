package kusuri12.teens_be.domain.auth.service;

import jakarta.transaction.Transactional;
import kusuri12.teens_be.domain.auth.domain.RefreshToken;
import kusuri12.teens_be.domain.auth.exception.AuthErrorCode;
import kusuri12.teens_be.domain.auth.presentation.dto.request.SignInRequest;
import kusuri12.teens_be.domain.auth.presentation.dto.response.SignInResponse;
import kusuri12.teens_be.domain.auth.repository.RefreshTokenRepository;
import kusuri12.teens_be.global.error.exception.TeensException;
import kusuri12.teens_be.global.security.userdetails.AuthDetails;
import kusuri12.teens_be.global.security.jwt.JwtTokenProvider;
import kusuri12.teens_be.global.security.jwt.JwtTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public SignInResponse execute(SignInRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();

            JwtTokens jwtTokens = jwtTokenProvider.generateToken(authDetails);
            refreshTokenRepository.save(jwtTokens.refreshToken());

            return new SignInResponse(jwtTokens.accessToken(), jwtTokens.refreshToken().getToken(), authDetails);
        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            throw new TeensException(AuthErrorCode.INVALID_CREDENTIALS);
        }
    }
}
