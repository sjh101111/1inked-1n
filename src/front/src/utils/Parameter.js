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