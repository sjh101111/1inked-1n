export const loginReqParam = (email, password) =>{
    const reqParam = {};
    reqParam.email = email;
    reqParam.password = password;

    return reqParam;
}

export const signupReqParam = (realName, email, password, passwordQuestionId, passwordQuestionAnswer) =>{
    const reqParam = {};
    reqParam.realName = realName;
    reqParam.email = email;
    reqParam.password = password;
    reqParam.passwordQuestionId = passwordQuestionId;
    reqParam.passwordQuestionAnswer = passwordQuestionAnswer;

    return reqParam;
}

export const changePasswordReqParam = () =>{
    const reqParam = {}
    reqParam.email = email;
    reqParam.passwordQuestionId = passwordQuestionId;
    reqParam.passwordQuestionAnswer = passwordQuestionAnswer;
    reqParam.newPassword = newPassword;

    return reqParam;
}

/**
 * @brief 프로필 저장 API는 form데이터 형식을 받으므로 이렇게 변경
 * @param {String} email 
 * @param {String} identity 
 * @param {String} location 
 * @param {String} description 
 * @param {File} file 
 * @returns 
 */
export const saveProfileReqParam = (email, identity, location, description, file) =>{
    const reqFormData = new FormData();

    reqFormData.append('email', email);
    reqFormData.append('identity', identity);
    reqFormData.append('location', location);
    reqFormData.append('description', description);
    reqFormData.append('file', file);

    return reqParam;
}

/**
 * 
 * @param {String} email 
 * @param {String} password 
 * @param {Boolean} withdraw 
 */
export const withdrawReqParam = (email, password, withdraw) =>{
    const reqParam = {};
    reqParam.email = email;
    reqParam.password = password;
    reqParam.withdraw = withdraw;

    return reqParam;
}

/**
 * multipart/form-data형태로 호출하는 API이므로 
 * @param {String} email 
 * @param {File} file 
 */
export const userImageUploadReqParam = (email, file) =>{
    const reqFormData = new FormData();

    reqFormData.append('email', email);
    reqFormData.append('file', file);

    return reqFormData;
}