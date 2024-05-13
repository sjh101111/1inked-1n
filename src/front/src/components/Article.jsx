import { Card, CardHeader, CardContent, CardFooter } from "./ui/card";
import { Avatar,AvatarImage, AvatarFallback } from "./ui/avatar";
import ArticleDialog from "./ArticleDialog";
import { Link } from "react-router-dom";
import { AlertDialog, AlertDialogTrigger, AlertDialogContent,AlertDialogHeader, AlertDialogTitle, AlertDialogDescription, AlertDialogFooter, AlertDialogCancel, AlertDialogAction } from "./ui/alert-dialog";
import { GenerateLiElUUID } from "@/utils/common";
import CommentCircleIcon from "./svg/CommentCircleIcon";
import { Comment } from "./Comment";

const Article = (props) =>{
    return (
        <Card key={GenerateLiElUUID()}>
            <CardHeader className="flex-row justify-between">
                <div className="flex items-center gap-4">
                    <Avatar className="w-20 h-20">
                        <AvatarImage alt="@shadcn" src="/placeholder-avatar.jpg" />
                        <AvatarFallback>hi</AvatarFallback>
                    </Avatar>
                    <div className="flex flex-col justify-center">
                        <h2 className="font-bold text-lg">username</h2>
                        <span className="text-black/50 ">id</span>
                    </div>
                </div>
                <div className="p-2">
                    <div className="modify-zone flex justify-end gap-4">
                        <ArticleDialog>
                            <Link>edit</Link>
                        </ArticleDialog>
                        {/* delete */}
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
                                        <AlertDialogAction onClick={() =>{alert("삭제 api호출")}}>삭제</AlertDialogAction>
                                    </AlertDialogFooter>
                                </AlertDialogContent>
                            </AlertDialog>
                    </div>
                    <span className="mt-2">2024.04.29 15:00</span>
                </div>
            </CardHeader>
            <CardContent className="p-16">
                <span>
                    Lorem, ipsum dolor sit amet consectetur adipisicing elit. Voluptas fuga earum aspernatur ea minus sint voluptatibus voluptates quam aliquid! Placeat, tempora. Corrupti sequi maiores assumenda ullam quasi nesciunt iure quod?Lorem, ipsum dolor sit amet consectetur adipisicing elit. Voluptas fuga earum aspernatur ea minus sint voluptatibus voluptates quam aliquid! Placeat, tempora. Corrupti sequi maiores assumenda ullam quasi nesciunt iure quod?Lorem, ipsum dolor sit amet consectetur adipisicing elit. Voluptas fuga earum aspernatur ea minus sint voluptatibus voluptates quam aliquid! Placeat, tempora. Corrupti sequi maiores assumenda ullam quasi nesciunt iure quod?Lorem, ipsum dolor sit amet consectetur adipisicing elit. Voluptas fuga earum aspernatur ea minus sint voluptatibus voluptates quam aliquid! Placeat, tempora. Corrupti sequi maiores assumenda ullam quasi nesciunt iure quod?Lorem, ipsum dolor sit amet consectetur adipisicing elit. Voluptas fuga earum aspernatur ea minus sint voluptatibus voluptates quam aliquid! Placeat, tempora. Corrupti sequi maiores assumenda ullam quasi nesciunt iure quod?Lorem, ipsum dolor sit amet consectetur adipisicing elit. Voluptas fuga earum aspernatur ea minus sint voluptatibus voluptates quam aliquid! Placeat, tempora. Corrupti sequi maiores assumenda ullam quasi nesciunt iure quod?Lorem, ipsum dolor sit amet consectetur adipisicing elit. Voluptas fuga earum aspernatur ea minus sint voluptatibus voluptates quam aliquid! Placeat, tempora. Corrupti sequi maiores assumenda ullam quasi nesciunt iure quod?
                </span>
                <ul className="mt-4 list-none grid grid-cols-3 justify-items-center gap-4">
                    <li className="w-[200px] h-[200px] bg-slate-400"></li>
                    <li className="w-[200px] h-[200px] bg-slate-400"></li>
                    <li className="w-[200px] h-[200px] bg-slate-400"></li>
                    <li className="w-[200px] h-[200px] bg-slate-400"></li>
                    <li className="w-[200px] h-[200px] bg-slate-400"></li>
                </ul>
            </CardContent>
            <CardFooter className="border-t p-3 pl-4 flex gap-4 items-center">
                <div className="flex items-center mt-1 gap-2 space-x-1 text-sm text-gray-500 cursor-pointer">
                    <CommentCircleIcon className={`h-6 w-6`} />
                    댓글보기
                </div>
            </CardFooter>
            <Comment></Comment>
       </Card> 
    );
};

export default Article;