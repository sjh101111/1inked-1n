import axios from "axios";
import mem from "mem";
import { getAccessToken, getRefreshToken, removeAccessToken, removeRefreshToken, setAccessToken, setRefreshToken } from "./Cookie";

const URL = "http://localhost:8082";

const instance = axios.create();

instance.interceptors.request.use((config) =>{
    const accessToken = getAccessToken();
    const refreshToken = getRefreshToken();

    if(accessToken){
        config.headers['Authorization'] = accessToken;
    }
    if(refreshToken){
        config.headers['Refresh-Token'] = refreshToken;
    }

    return config;
}, (err) =>{ return Promise.reject(err);});

instance.interceptors.response.use(
    (res) => res,
    async (err) => {
        const { config, response: {status, data} } = err;

        if(status === 401 && data.message === "Access token is invalid or expired"){
            try{
                const newTokenResult = await getNewAccessToken();
                if(newTokenResult.status === 200){
                    const { accessToken, refreshToken} = newTokenResult.data;
                    setAccessToken(accessToken);
                    setRefreshToken(refreshToken);

                    return instance(config);
                }else{
                    logout();
                }
            } catch (e){
                logout();
            }
        }

        if(status === 401 && data.message === "Refresh token is invalid or expired"){
            logout();
        }

        return Promise.reject(err);       
    }
)

/**
 * 
 * @param {*} url 
 * @param { FormData } formData 
 * @brief 이미지 파일과 reqParam을 같이 보내야할 때 사용
 */
export const OneinkedMultipart = (url, formData, method = false) =>{
    const requestor = method ? instance.put : instance.post;
    return requestor(url, formData, {
        headers: { "Content-Type" : "multipart/form-data"}
    });
}


export const OneinkedPost = (url, body) =>{
    return instance.post(url, body);
}

export const OneinkedGet = (url, params = {}) => {
    return instance.get(url, params);
}

export const OneinkedDelete = (url, params = {}) =>{
    return instance.delete(url, params);
}

export const OneinkedPut = (url, body, params = {}) =>{
    return instance.put(url, body, params);
}

export const OneinkedPatch = (url, body) =>{
    return instance.patch(url, body);
}

//메모미제이션을 통한 과도한 AccessToken 재발급 문제 처리
export const getNewAccessToken = mem(async () =>{
    const newAccessTokenURL = URL + "/refresh";
    return OneinkedPost(newAccessTokenURL);
}, {memAge: 1000});

/** User API START */

//회원가입
export const signup = async (signupReqParam) =>{
    const signupURL = URL + "/api/user";
    return OneinkedPut(signupURL, signupReqParam)
    .then((response) => response.data);
}

//로그인
export const login = async (loginReqParam) =>{
    const loginURL = URL + "/login";
    return OneinkedPost(loginURL, loginReqParam)
    .then((response) => {
        console.log("Authorization:", response.headers['authorization']);
        console.log("Refresh-Token:", response.headers['refresh-token']);
        setAccessToken(response.headers['authorization']);
        setRefreshToken(response.headers['refresh-token']);
        return response.data;
    });
}

//비밀번호 찾기 질문 리스트 조회
/**
 * @returns { {id: String, question: String}[]}
 */
export const fetchPasswordQuestions = async () =>{
    const passwordQuestionURL = URL + "/api/passwordquestion";

    return OneinkedGet(passwordQuestionURL)
    .then((response) => response.data);
}

/**
 * 
 * @param {{email: String, passwordQuestionId: String, passwordQuestionAnswer: String, newPassword: String}} changePasswordReqParam 
 * @returns 
 */
export const changePassword = async (changePasswordReqParam) =>{
    const changePasswordURL = URL + "/api/password";

    return OneinkedPost(changePasswordURL, changePasswordReqParam)
    .then((response) => response.data);
}

/**
 * @breif 로그인한 유저의 profile data load 
 */
export const fetchLoginUserProfile = async () =>{
    const fetchLoginUserProfileURL = `${URL}/api/user`;

    return OneinkedGet(fetchLoginUserProfileURL)
    .then((response) => response.data);
}

/**
 * @breif 다른 유저의 profile data load
 */
export const fetchAnotherUserProfile = async (email) =>{
    const fetchAnotherUserProfileURL = URL + `/api/user?email=${email}`;

    return OneinkedGet(fetchAnotherUserProfileURL)
    .then((response) =>  response.data);
}


export const saveProfile = async (saveProfileReqParam) =>{
    const saveProfileURL = URL + "/api/profile";

    return OneinkedMultipart(saveProfileURL, saveProfileReqParam)
    .then((response) => response.data);
}

export const withdraw = async (withdrawReqParam) =>{
    const withdrawURL = URL + "/api/withdraw";

    return OneinkedPost(withdrawURL, withdrawReqParam)
    .then((response) => response.data);
}

export const userImageUpload = async (userImageUploadReqParam) =>{
    const userImageUploadURL = URL + "/api/user/image";

    return OneinkedMultipart(userImageUploadURL, userImageUploadReqParam)
    .then((response) => response.data);
}

export const logout = () =>{
    //토큰 삭제 이후 "/"로 이동
    removeAccessToken();
    removeRefreshToken();
    location.href = location.origin;
}
/** User API END */

/** Article API START */

// 게시글 생성 API
export const createArticle = async (createArticleReqParam) =>{
    const createArticleURL = URL + "/api/article";

    return OneinkedMultipart(createArticleURL, createArticleReqParam)
    .then((response) => response.data);
}

//내 게시글 전체 조회 API
export const readAllMyArticle = async () =>{
    const readAllMyArticleURL = URL + "/api/myAllArticles";

    return OneinkedGet(readAllMyArticleURL)
    .then((response) => response.data);
}

//메인 화면 게시글 조회 API
export const readMainFeedArticles = async () =>{
    const readMainFeedArticlesURL = URL + "/api/mainFeedArticles";

    return OneinkedGet(readMainFeedArticlesURL)
    .then((response) => response.data);
}

export const updateArticle = async (articleId, updateArticleReqParam) =>{
    const updateArticleURL = `${URL}/api/article/${articleId}`;

    return OneinkedMultipart(updateArticleURL, updateArticleReqParam, true)
    .then((response) => response.data);
}

export const deleteArticle = async (articleId) =>{
    const deleteArticleURL = `${URL}/api/article/${articleId}`;

    return OneinkedDelete(deleteArticleURL)
    .then((response) => response.data);
}
/** Article API END */

/** Comment API START */

export const addComment = async (articleId, addCommentReqParam) =>{
    const addCommentURL =   `${URL}/api/comment/${articleId}`;

    return OneinkedPost(addCommentURL, addCommentReqParam)
    .then((response) => response.data);
}

export const readComment = async (articleId) =>{
    const readCommentURL =  `${URL}/api/comment/${articleId}`;

    return OneinkedGet(readCommentURL)
    .then((response) => response.data);
}

export const updateComment = async (commentId, updateCommentReqParam) =>{
    const updateCommentURL = `${URL}/api/comment/${commentId}`;

    return OneinkedPatch(updateCommentURL, updateCommentReqParam)
    .then((response) => response.data);
}

export const deleteComment = async (commentId) =>{
    const deleteCommentURL = `${URL}/api/comment/${commentId}`;

    return OneinkedDelete(deleteCommentURL)
    .then((response) => response.data);
}

/** Comment API END */

/** Naver News API START*/
export const fetchNewsItems = async(newsParams) =>{
    const newsRequstUrl = URL+`/naver-news?query=${newsParams.query}`
    +`&page=${newsParams.page}`+`&sort=${newsParams.sort}`;

    return OneinkedGet(newsRequstUrl)
    .then((response) => response.data);
}
/** Naver News API END */


/** Chat API START */
export const createChat = async (addChatReqParam) =>{
    const createChatURL = URL + "/api/createChat";

    return OneinkedPost(createChatURL, addChatReqParam)
        .then((response) => response.data)
}

export const readChatWithPartner = async (partnerEmail) =>{
    const readChatWithPartnerURL = URL + "/api/chatWithPartner"

    return OneinkedGet(readChatWithPartnerURL, { params: { partnerEmail } })
        .then((response) => response.data)
}

export const readChatSummaries = async () => {
    const readChatSummariesURL = URL + "/api/chatSummaries"

    return OneinkedGet(readChatSummariesURL)
        .then((response) => response.data)
};

export const updateIsDeleted = async (partnerEmail) => {
    const updateIsDeletedURL = URL + "/api/updateIsDeleted"

    return OneinkedPut(updateIsDeletedURL,{}, {params : {partnerEmail}})
        .then((response) => response.data)
}

export const deleteChat = async (partnerEmail) => {
    const deleteChatURL = URL + "/api/deleteChat"

    return OneinkedDelete(deleteChatURL, {params: {partnerEmail}})
        .then((response) => response.data)
}
/** Chat API END */

/** Allen Ai Api START */
export const getReviewedResumeByAllenAI = async (client_id, content) => {
    const getReviewedResumeURL = URL + "/api/resumeReview"
    const params = {
        client_id: client_id,
        content: content
    };
    return OneinkedGet(getReviewedResumeURL, {params})
        .then((response) => response.data)
}
/** Allen Ai Api END */

/** Resume API START */
export const saveResume = async (addResumeRequestDto) => {
    const saveResumeURL = URL + "/api/resume"

    return OneinkedPost(saveResumeURL, addResumeRequestDto)
        .then((response) => response.data)
}
/** Resume API END */