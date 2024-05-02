import { Button } from "./ui/button";
import { Textarea } from "./ui/textarea";
import { Avatar, AvatarFallback } from "./ui/avatar";

const CommentItem = ({commentId, nickname ="temp", email= "temp", commentDate = "now", commentContent ="test"}) =>{
    return (
        <li className="space-y-4">
            <div className="w-full flex items-start space-x-3">
                <Avatar>
                    <AvatarFallback className="font-bold">hi</AvatarFallback>
                </Avatar>
                <div className="w-full">
                    <div className="w-full flex justify-between items-center">
                        <p className="text-sm font-semibold">{`${nickname}(${email || "비회원"})`}</p>
                        <div className="modi-zone flex gap-2 text-sm">
                            <span>edit</span>
                            <span>delete</span>
                        </div>
                    </div>
                    <p className="text-xs text-gray-500">{commentDate}</p>
                    <p className="mt-1 text-sm">{commentContent}</p>
                </div>
            </div>
        </li>
    );
};

const Comment = () => {
    const commentInfoList = [
        <CommentItem></CommentItem>,
        <CommentItem></CommentItem>
    ];

    return (
        <div className="p-4 border-t">
            <ul className="flex flex-col gap-4">
                {
                    commentInfoList.map(commentInfo =>{
                        return (<CommentItem key={commentInfo.commentId} {...commentInfo}></CommentItem>);
                    })
                }
            </ul>
            <Textarea onChange={(ev) => setContent(ev.target.value)} className="mt-4 resize-none" placeholder="댓글을 입력해주세요"></Textarea>
            <Button asChild className="mt-2 bg-black text-white w-full">
                <div onClick={() =>{}}>댓글 등록</div>
            </Button>
        </div>
    );
};

export {
    Comment,
    CommentItem
}