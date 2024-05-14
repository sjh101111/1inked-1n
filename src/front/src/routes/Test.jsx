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
import { useEffect, useState } from "react";
import Login from "./Login";
import Article from "@/components/Article";
import Header from "@/components/Layout/Header";
import Chat from "./Chat";
import ChatDialog from "@/components/ChatDialog";
import ChatBox from "@/components/ChatBox";
import FollowInfo from "@/components/FollowInfo";
import MyResumes from "@/components/MyResumes";
import UserPage from "./UserPage";
import { createArticle, deleteArticle, fetchLoginUserProfile, readAllMyArticle, readMainFeedArticles, saveProfile, updateArticle, withdraw } from "@/utils/API";
import { createArticleReqParam, saveProfileReqParam, updateArticleReqParam, withdrawReqParam } from "@/utils/Parameter";
import { getAccessToken, getAccessTokenInfo } from "@/utils/Cookie";
import { getArticleItems } from "@/utils/Items";


const Test =  () => {
    const email = "dlxogml11235@naver.com";
    const [file, setFile] = useState(null);

    /**아티클 API 테스트 */
    const [articles, setArticles] = useState([]);
    const [articleItems, setArticleItems] = useState(<></>);
    const [articleFiles, setArticleFiles] = useState([]);

    const resource = {
        btnText: "생성",
        clickCallback: () =>{
            const file = [...document.getElementById('picture').files];
            alert("save!")
        },
        initFn: () => {}
    }

    useEffect(() =>{
       getAccessTokenInfo(); 
    },[]);

    const userProfileDemo = () =>{

        fetchLoginUserProfile()
        .then((data) => {console.log(data)});
    }

    const userWithdraw = (bool) =>{
        const reqParam = withdrawReqParam("Dlxogml!135", bool);

        withdraw(reqParam)
        .then(data => console.log(data));
    }

    const saveUserProfile = () =>{
        const reqParam = saveProfileReqParam("123","서울","ㄷㅈ매럊믇ㄹ",file);

        saveProfile(reqParam)
        .then(response => console.log(response));
    }

    const showMainFeedArticles = () =>{
        readMainFeedArticles()
        .then((data) => console.log(data));
    }

    const showAllMyArticles = () =>{
        readAllMyArticle()
        .then((response) => {
            setArticles([...response]);
            setArticleItems(getArticleItems([...response]));
            console.log(response);
        }
        );
    }

    const createMainFeedArticle = () =>{
        const reqParam = createArticleReqParam("안녕하세요", articleFiles);

        createArticle(reqParam)
        .then(response =>{
            console.log(response);
        });
    }

    const updateWritenArticle = () =>{
        console.log(articles);
        if(articles.length == 0){
            alert("내 아티클 조회API를 호출해주세요.");
            return ;
        }

        const firstArticleId = articles[0].id;
        //현재 첨부한 파일들로 reqParam 불러온다.
        const reqParam = updateArticleReqParam(Math.random().toString(36).substring(2, 12), articleFiles);
        updateArticle(firstArticleId, reqParam)
        .then(data => console.log(data));
    }

    const deleteWritenArticle = () =>{
        if(articles.length == 0){
            alert("조회된 Article이 없습니다.");
            return ;
        }

        const deleteArticleId = articles[0].id;
        deleteArticle(deleteArticleId)
        .then(data => console.log(data));
    }

    return (
        <main className="">
            <div>
                <h2>유저 API 테스트</h2>
                <Button onClick={userProfileDemo}>유저 프로필 조회</Button>
                <Button onClick={() =>userWithdraw(true)}>회원 탈퇴 API true</Button>
                <Button onClick={() =>userWithdraw(false)}>회원 탈퇴 API false</Button>

                <Input type="file" onChange={ev =>setFile(ev.target.files[0])}></Input>
                <Button onClick={saveUserProfile}>유저 프로필 저장</Button>
            </div>
            <div className="mt-4 grid gap-4">
                <h2>아티클 API 테스트</h2>
                <Button onClick={showMainFeedArticles}>아티클 조회 API</Button>
                <Button onClick={showAllMyArticles}>내가 쓴 아티클 조회 API</Button>
                <Button onClick={createMainFeedArticle}>아티클 생성 API</Button>
                <Button onClick={updateWritenArticle}>내가 쓴 아티클 수정 API(내가 쓴 아티클 조회 API 먼저 누를것)</Button>
                <Button onClick={deleteWritenArticle}>내가 쓴 아티클 삭제 API(내가 쓴 아티클 조회 API 먼저 누를것)</Button>

                <Input type="file" accept=".png" multiple onChange={ev => {setArticleFiles([...ev.target.files] || []);}}></Input>
            </div>
            <div>
                {
                    articleItems
                }
            </div>
        </main>
        
    );
};

export default Test;