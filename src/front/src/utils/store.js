import create from "zustand";
import { persist } from "zustand/middleware"

//로그인 상태관리
export const useLogin = create(persist((set) => ({
    isLogin: false,
    setLogin: login => set(state =>({
        isLogin: login
        }))
    }),
    {
        name: 'userLoginStorage',
    }
));

//유저정보 상태관리
export const useUserInfo = create(persist((set, get) => ({
        userInfo: {
            id: '',
            realName: '',
            email:'',
            identity: '',
            location: '',
            description: '',
            profileSrc: ''
        },
        setUserInfo: info => set(state => ({
            userInfo: info
        })),
    }),
    {
        name: "userInfoStorage",
    }
));