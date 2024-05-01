import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";
import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
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
import PasswordChange from "@/routes/PasswordChange.jsx";
import Resign from "@/routes/Resign.jsx";

const Test =  () => {
    const resource = {
        btnText: "생성",
        clickCallback: () =>{
            const file = [...document.getElementById('picture').files];
            alert("save!")
        },
        initFn: () => {}
    }

    return (
        <Resign></Resign>
    );
};

export default Test;