import { Card, CardHeader, CardContent, CardFooter } from "./ui/card";
import { Avatar,AvatarImage, AvatarFallback } from "./ui/avatar";
import ArticleDialog from "./ArticleDialog";
import { Link } from "react-router-dom";
import { AlertDialog, AlertDialogTrigger, AlertDialogContent,AlertDialogHeader, AlertDialogTitle, AlertDialogDescription, AlertDialogFooter, AlertDialogCancel, AlertDialogAction } from "./ui/alert-dialog";
import { FullDateFormatString, GenerateLiElUUID } from "@/utils/common";
import CommentCircleIcon from "./svg/CommentCircleIcon";
import { Comment } from "./Comment";
import { useEffect, useState } from "react";
import { MessageCircleIcon, MessageCircleOffIcon } from "lucide-react";
import { getArticlePictures } from "@/utils/Items";
import { getAccessTokenInfo } from "@/utils/Cookie";
import { deleteArticle } from "@/utils/API";

const Article = ({id, contents, createdAt, updatedAt, images, user, afterDeleteFn}) =>{
    const [commentVisibility, setCommentVisibility] = useState(false);

    //유저 프로필 이미지
    const [profilePic, setProfilePic] = useState("");

    //아티클 첨부 이미지들
    const [articlePicItems, setArticlePicItems] = useState([]);

    //jwt Token의 유저 정보와 일치하는지 확인하는 메서드
    const [articleOwner, setArticleOwner] = useState(false);


    useEffect(() =>{
        //유저 프로필 이미지가 있다면
        if(user?.image){
            setProfilePic(`data:image/png;base64,${user.image}`);
        }

        //아티클 첨부 이미지 업데이트
        if(images){
            setArticlePicItems(getArticlePictures(images));
        }

        isWirter();
    },[]);

    const toggleCommentVisibility = () =>{
        setCommentVisibility(!commentVisibility);
    }

    const isWirter = () =>{
        const {sub} = getAccessTokenInfo();
        if(sub === user.email){
            setArticleOwner(true)
        }
    }

    const doDelete = () =>{
        deleteArticle(id)
        .then(() =>{
            alert("삭제가 완료되었습니다.");
            afterDeleteFn(id);
        })
        .catch(e => console.log(e))
    }

    return (
        <Card key={id}>
            <CardHeader className="flex-row justify-between">
                <div className="flex items-center gap-4">
                    <Avatar className="w-20 h-20">

                        <AvatarImage alt="유저 프로필이미지" src={profilePic}/>
                        <AvatarFallback></AvatarFallback>
                    </Avatar>
                    <div className="flex flex-col justify-center">
                        <h2 className="font-bold text-lg">{user.realName}</h2>
                        <span className="text-black/50 ">{user.email}</span>
                    </div>
                </div>
                <div className="p-2">
                    <div className="modify-zone flex justify-end gap-4">
                        {
                            articleOwner ?
                                <>
                                    <ArticleDialog {...{id, contents, user}}>
                                        <Link>edit</Link>
                                    </ArticleDialog>
                                    <AlertDialog>
                                        <AlertDialogTrigger asChild>
                                            <Link>delete</Link>
                                        </AlertDialogTrigger>
                                        <AlertDialogContent>
                                            <AlertDialogHeader>
                                                <AlertDialogTitle>
                                                    정말 게시글을 삭제하시겠어요?
                                                </AlertDialogTitle>
                                                <AlertDialogDescription>
                                                    게시글을 삭제하면 되돌릴 수 없어요. 신중하게 선택해주세요. 정말 삭제하시겠어요?
                                                </AlertDialogDescription>
                                            </AlertDialogHeader>
                                            <AlertDialogFooter>
                                                <AlertDialogCancel>취소</AlertDialogCancel>
                                                <AlertDialogAction onClick={doDelete}>삭제</AlertDialogAction>
                                            </AlertDialogFooter>
                                        </AlertDialogContent>
                                    </AlertDialog>
                                </> :
                                <></>
                        }
                    </div>
                    <span className="mt-2">
                        {FullDateFormatString(new Date(updatedAt))}
                    </span>
                </div>
            </CardHeader>
            <CardContent className="p-8">
                <span>
                   {contents}
                </span>
                <ul className="mt-4 list-none grid grid-cols-3 justify-items-center gap-4">
                    {articlePicItems}
                </ul>
            </CardContent>
            <CardFooter className="border-t p-3 pl-4 flex gap-4 items-center">
                <div onClick={toggleCommentVisibility} className="flex items-center mt-1 gap-2 space-x-1 text-sm text-gray-500 cursor-pointer">
                    {
                        commentVisibility ? 
                        <>
                            <MessageCircleOffIcon className={`h-6 w-6`}></MessageCircleOffIcon> 
                            댓글 닫기
                        </> :
                        <>
                            <MessageCircleIcon className={`h-6 w-6`} />
                            댓글 보기
                        </>
                    }
                </div>
            </CardFooter>
            {
                commentVisibility ? 
                <Comment articleId={id}></Comment> :
                <></>
            }
       </Card> 
    );
};

export default Article;