package kusuri12.teens_be.domain.information.service;

import kusuri12.teens_be.domain.information.exception.InfoErrorCode;
import kusuri12.teens_be.domain.information.presentation.dto.request.CreateInformationRequest;
import kusuri12.teens_be.domain.information.presentation.dto.request.UpdateInformationRequest;
import kusuri12.teens_be.domain.information.presentation.dto.response.InformationDetailResponse;
import kusuri12.teens_be.domain.information.presentation.dto.response.InformationListResponse;
import kusuri12.teens_be.domain.information.domain.Information;
import kusuri12.teens_be.domain.information.repository.InformationRepository;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.exception.UserErrorCode;
import kusuri12.teens_be.domain.user.repository.UserRepository;
import kusuri12.teens_be.global.error.exception.TeensException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InformationService {

    private final InformationRepository infoArticleRepository;
    private final UserRepository userRepository;

    // todo: 페이징 처리하기
    // 게시글 전체 조회
    public List<InformationListResponse> getAllInfoArticles() {
        List<Information> articles = infoArticleRepository.findAllByOrderByCreatedAtDesc();

        return articles.stream()
                .map(article -> InformationListResponse.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .authorName(article.getUser().getNickname())
                        .createdAt(article.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // 검색
    public List<InformationListResponse> searchInfoArticles(String keyword) {
        List<Information> articles = infoArticleRepository.searchByKeyword(keyword);

        return articles.stream()
                .map(article -> InformationListResponse.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .authorName(article.getUser().getNickname())
                        .createdAt(article.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // 게시글 상세 조회
    public InformationDetailResponse getInfoArticleDetail(Long articleId) {
        Information article = infoArticleRepository.findById(articleId)
                .orElseThrow(() -> new TeensException(InfoErrorCode.INFO_NOT_FOUND));

        return InformationDetailResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .authorName(article.getUser().getNickname())
                .createdAt(article.getCreatedAt())
                .build();
    }

    // 게시글 생성
    @Transactional
    public void createInfoArticle(Long userId, CreateInformationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TeensException(UserErrorCode.USER_NOT_FOUND));

        Information article = Information.builder()
                .title(request.title())
                .content(request.content())
                .user(user)
                .build();

        infoArticleRepository.save(article);
    }

    @Transactional
    public void updateInfoArticle(Long requestUserId, Long articleId, UpdateInformationRequest request) {
        Information article = infoArticleRepository.findById(articleId)
                .orElseThrow(() -> new TeensException(InfoErrorCode.INFO_NOT_FOUND));

        if (!article.getUser().getId().equals(requestUserId)) {
            throw new TeensException(InfoErrorCode.NO_AUTHOR);
        }

        article.updateTitleAndContent(request.title(), request.content());
    }

    // todo: aop 적용해서 검사 로직 빼버리기
    @Transactional
    public void deleteInfoArticle(Long requestUserId, Long articleId) {
        Information article = infoArticleRepository.findById(articleId)
                .orElseThrow(() -> new TeensException(InfoErrorCode.INFO_NOT_FOUND));

        if (!article.getUser().getId().equals(requestUserId)) {
            throw new TeensException(InfoErrorCode.NO_AUTHOR);
        }

        infoArticleRepository.delete(article);
    }
}