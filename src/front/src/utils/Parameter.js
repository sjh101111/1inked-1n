/** User ReqParam START */
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

export const changePasswordReqParam = (email, passwordQuestionId, passwordQuestionAnswer, newPassword) =>{
    const reqParam = {};

    reqParam.email = email;
    reqParam.passwordQuestionId = passwordQuestionId;
    reqParam.passwordQuestionAnswer = passwordQuestionAnswer;
    reqParam.newPassword = newPassword;

    return reqParam;
}

/**
 * @brief 프로필 저장 API는 form데이터 형식을 받으므로 이렇게 변경
 * @param {String} identity 
 * @param {String} location 
 * @param {String} description 
 * @param {File} file 
 * @returns 
 */
export const saveProfileReqParam = (identity, location, description, file) =>{
    const reqFormData = new FormData();

    reqFormData.append('identity', identity);
    reqFormData.append('location', location);
    reqFormData.append('description', description);
    reqFormData.append('file', file);

    return reqFormData;
}

/**
 * 
 * @param {String} password 
 * @param {Boolean} withdraw 
 */
export const withdrawReqParam = (password, withdraw) =>{
    const reqParam = {};
    reqParam.password = password;
    reqParam.withdraw = withdraw;

    return reqParam;
}

/**
 * multipart/form-data형태로 호출하는 API이므로 
 * @param {File} file 
 */
export const userImageUploadReqParam = (file) =>{
    const reqFormData = new FormData();

    reqFormData.append('file', file);

    return reqFormData;
}

/** User ReqParam End */

/** Article ReqParam START */

/**
 * @param {String} contents 
 * @param {File[]} files 
 * @returns 
 */
export const createArticleReqParam = (contents, files) =>{
    const reqFormData = new FormData();

    reqFormData.append('contents', contents);

    files.forEach(file => {
        reqFormData.append("files", file);
    });

    return reqFormData;
}

/**
 * @param {String} contents 
 * @param {File[]} files 
 */
export const updateArticleReqParam = (contents, files) =>{
    const reqFormData = new FormData();

    reqFormData.append('contents', contents)
    
    files.forEach(file =>{
        reqFormData.append('file', file);
    });

    return reqFormData;
}
/** Article ReqParam END */

/** Comment ReqParam START */

/**
 * 
 * @param {String} comments 
 * @param {String} parentId 
 */
export const addCommentReqParam = (comments, parentId) =>{
    const reqParam = {};
    
    reqParam.comments = comments;
    reqParam.parentId = parentId;

    return reqParam;
}

export const updateCommentReqParam = (comments) =>{
    const reqParam = {};

    reqParam.comments = comments;

    return reqParam;
}

/** Comment ReqParam END */