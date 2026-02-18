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
import kusuri12.teens_be.global.security.annotation.CheckAuthor;
import kusuri12.teens_be.global.security.annotation.CheckId;
import kusuri12.teens_be.global.security.aspect.Authorizable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InformationService implements Authorizable {

    private final InformationRepository infoArticleRepository;
    private final UserRepository userRepository;

    // 게시글 전체 조회
    public Page<InformationListResponse> getAllInfoArticles(Pageable pageable) {
        Page<Information> articles = infoArticleRepository.findAllByOrderByCreatedAtDesc(pageable);
        return articles.map(InformationListResponse::of);
    }

    // 검색
    public Page<InformationListResponse> searchInfoArticles(String keyword, Pageable pageable) {
        Page<Information> articles = infoArticleRepository.searchByKeyword(keyword, pageable);
        return articles.map(InformationListResponse::of);
    }

    // 게시글 상세 조회
    public InformationDetailResponse getInfoArticleDetail(Long articleId) {
        Information article = getInfoArticle(articleId);
        return InformationDetailResponse.from(article);
    }

    // 게시글 생성
    @Transactional
    public void createInfoArticle(Long userId, CreateInformationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TeensException(UserErrorCode.USER_NOT_FOUND));

        Information article = Information.of(request.title(), request.content(), user);
        infoArticleRepository.save(article);
    }

    @Transactional
    @CheckAuthor
    public void updateInfoArticle(@CheckId Long articleId, UpdateInformationRequest request) {
        Information article = getInfoArticle(articleId);
        article.updateTitleAndContent(request.title(), request.content());
    }

    @Transactional
    @CheckAuthor
    public void deleteInfoArticle(@CheckId Long articleId) {
        Information article = getInfoArticle(articleId);
        infoArticleRepository.delete(article);
    }

    private Information getInfoArticle(Long articleId) {
        return infoArticleRepository.findById(articleId)
                .orElseThrow(() -> new TeensException(InfoErrorCode.INFO_NOT_FOUND));
    }

    @Override
    public Long getAuthorId(Long resourceId) {
        return infoArticleRepository.findById(resourceId)
                .map(article -> article.getUser().getId())
                .orElseThrow(() -> new TeensException(InfoErrorCode.INFO_NOT_FOUND));
    }
}