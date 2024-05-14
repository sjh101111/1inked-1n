import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTrigger } from "@/components/ui/dialog";
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar";
import { Textarea } from "@/components/ui/textarea";
import FileInput from "@/components/FileInput";
import { Button } from "@/components/ui/button";
import { useState, useEffect } from "react";
import { createArticle, fetchLoginUserProfile, updateArticle } from "@/utils/API";
import { createArticleReqParam, updateArticleReqParam } from "@/utils/Parameter";
import { getAccessTokenInfo } from "@/utils/Cookie";

const ArticleDialog = (props) =>{
    const [open, setOpen] = useState(false);

    //여기서 유저정보를 어떻게 ..?
    const {id, user, contents} = props;
    const [userInfo, setUserInfo] = useState(null);
    const [profilePic, setProfilePic] = useState("");
    //textArea
    const [textContents, setTextContents] = useState("");
    const [files, setFiles] = useState([]);
    const [mode, setMode] = useState("create");

    useEffect(() =>{
        setTextContents("");
        setFiles([]);
        setMode("create");
        setUserInfo(null);
        setProfilePic("");
    }, [open]);

    const fetchUserInfo = (dialogIsOpen) =>{
        fetchLoginUserProfile()
        .then(async (userInfo) =>{
            setUserInfo(userInfo);

            if(id){
                handleEditMode();
            }

            setProfilePic(`data:image/png;base64,${userInfo.image}`);
        });
    }

    const doArticleCreate = () =>{
        const reqParam = createArticleReqParam(textContents, files);

        createArticle(reqParam)
        .then((data) => {
            setOpen(false);
        })
    }

    const doArticleUpdate = () =>{
        const reqParam = updateArticleReqParam(textContents, files);

        updateArticle(id, reqParam)
        .then((data) =>{
            console.log(data);
            setOpen(false);
        })
    }

    const handleEditMode = () =>{
        setMode("edit");
        setTextContents(contents);
    }

    return (
        <Dialog open={open} onOpenChange={(isOpen) =>{setOpen(isOpen); fetchUserInfo();}}>
            <DialogTrigger asChild {...props}></DialogTrigger>
            <DialogContent onPointerDownOutside={ev => ev.preventDefault()}>
                <DialogHeader>
                    <div className="flex items-center gap-4">
                        <Avatar className="w-12 h-12">
                            <AvatarImage alt="유저 프로필 이미지" src={profilePic} />
                            <AvatarFallback></AvatarFallback>
                        </Avatar>
                        <div className="flex flex-col justify-center">
                            <h2 className="font-bold text-lg">{userInfo?.realName}</h2>
                            <span className="text-black/50 ">{userInfo?.email}</span>
                        </div>
                    </div>
                </DialogHeader>
                <Textarea onChange={ev => {setTextContents(ev.target.value)}} value={textContents} className="mt-4 h-48 resize-none" placeholder="input your content"></Textarea>
                <FileInput onChange={setFiles}></FileInput>
                <DialogFooter>
                    <Button className="bg-[#6866EB]" onClick={mode === 'create' ? doArticleCreate : doArticleUpdate} type="submit">{mode === 'create' ? '생성' : '수정'}</Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
};

export default ArticleDialog;