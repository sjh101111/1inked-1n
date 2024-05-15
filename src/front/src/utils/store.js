import { create } from "zustand";
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

export const useFollower = create(persist((set, get) => ({
        followerInfo: {

        },
        setFollowerInfo: (info) => set(state => ({
            followerInfo: info
        })),
    }),
    {
        name: "followerInfoStorage"
    }
));

export const useFollowee = create(persist((set, get) => ({
        followeeInfo: {

        },
        setFolloweeInfo: (info) => set(state => ({
            followeeInfo: info
        })),
    }),
    {
        name: "followeeInfoStorage"
    }
));