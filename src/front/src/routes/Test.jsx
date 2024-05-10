import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";
import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { GenerateLiElUUID } from "@/utils/common";
import { Link } from "react-router-dom";
import ArticleDialog from "@/components/ArticleDialog";
import { AlertDialog, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger, AlertDialogAction } from "@/components/ui/alert-dialog";
import { Textarea } from "@/components/ui/textarea";
import CommentCircleIcon from "@/components/svg/CommentCircleIcon";
import { useState } from "react";
import Login from "./Login";
import Article from "@/components/Article";
import Header from "@/components/Layout/Header";
import Chat from "./Chat";
import ChatDialog from "@/components/ChatDialog";
import ChatBox from "@/components/ChatBox";
import FollowInfo from "@/components/FollowInfo";
import MyResumes from "@/components/MyResumes";
import UserPage from "./UserPage";
import { fetchUserProfile, saveProfile, withdraw } from "@/utils/API";
import { saveProfileReqParam, withdrawReqParam } from "@/utils/Parameter";


const Test =  () => {
    const email = "dlxogml11235@naver.com";
    const [file, setFile] = useState(null);

    const resource = {
        btnText: "생성",
        clickCallback: () =>{
            const file = [...document.getElementById('picture').files];
            alert("save!")
        },
        initFn: () => {}
    }

    const userProfileDemo = () =>{

        fetchUserProfile(email)
        .then((data) => {console.log(data)});
    }

    const userWithdraw = (bool) =>{
        const reqParam = withdrawReqParam("dlxogml11235@naver.com", "Dlxogml!135", bool);

        withdraw(reqParam)
        .then(data => console.log(data));
    }

    const saveUserProfile = () =>{
        const reqParam = saveProfileReqParam(email, "123","서울","ㄷㅈ매럊믇ㄹ",file);

        saveProfile(reqParam)
        .then(response => console.log(response));
    }

    return (
        <div>
            <Button onClick={userProfileDemo}>유저 프로필 조회</Button>
            <Button onClick={() =>userWithdraw(true)}>회원 탈퇴 API true</Button>
            <Button onClick={() =>userWithdraw(false)}>회원 탈퇴 API false</Button>

            <Input type="file" onChange={ev =>setFile(ev.target.files[0])}></Input>
            <Button onClick={saveUserProfile}>유저 프로필 저장</Button>
        </div>
    );
};

export default Test;