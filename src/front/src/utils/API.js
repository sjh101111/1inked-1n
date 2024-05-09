import axios from "axios";
import mem from "mem";
import { getAccessToken, getRefreshToken, removeAccessToken, removeRefreshToken, setAccessToken, setRefreshToken } from "./Cookie";

const URL = "http://localhost:8080";

const instance = axios.create();

instance.interceptors.request.use((config) =>{
    const accessToken = getAccessToken();
    const refreshToken = getRefreshToken();

    if(accessToken){
        config.headers['Authorization'] = `Bearer ${accessToken}`
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
export const OneinkedMultipart = (url, formData) =>{
    return instance.post(url, formData, {
        headers: { "Content-Type" : "multipart/form-data"}
    });
}

export const OneinkedPost = (url, body) =>{
    return instance.post(url, body);
}

export const OneinkedGet = (url) => {
    return instance.get(url);
}

export const OneinkedDelete = (url) =>{
    return instance.delete(url);
}

export const OneinkedPut = (url, body) =>{
    return instance.put(url, body);
}

//메모미제이션을 통한 과도한 AccessToken 재발급 문제 처리
export const getNewAccessToken = mem(async () =>{
    const newAccessTokenURL = URL + "/refresh";
    return OneinkedPost(newAccessTokenURL);
}, {memAge: 1000});


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
    .then((response) => response.data)
    .then(json =>{
        setAccessToken(json.accessToken);
        setRefreshToken(json.refreshToken);
    });
}

//비밀번호 찾기 질문 리스트 조회
/**
 * @returns { {id: String, question: String} Array}
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