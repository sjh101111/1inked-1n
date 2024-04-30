import { Send } from "lucide-react";
import { Button } from "./ui/button";
import { Link } from "react-router-dom";
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger } from "./ui/alert-dialog";
import { useState, useEffect } from "react";

const ChatBox = (props) =>{
    const [selectBoxItem, setBoxItem] = useState(null);

    const tempChkBoxItems = Array(10).fill(<CheckBoxItem setBoxItem={setBoxItem}></CheckBoxItem>);
    return (
        <section className="chat p-5 flex h-[calc(100%-60px)] gap-10">
            <section className={`rounded-lg p-4 flex flex-col w-2/5 border`}>
                <h2 className="text-lg font-bold">쪽지함</h2>
                <div className="mt-4 flex flex-col overflow-auto flex-grow gap-4">
                   {
                        tempChkBoxItems.map(item => item)
                   } 
                </div>
            </section>
            <ChatBoxDetail selectBoxItem={selectBoxItem}></ChatBoxDetail>
        </section>
    );
};

const CheckBoxItem = ({srcUser="", destUser="", date=new Date(), content="lorem", setBoxItem}) =>{
    return (
        <div onClick={setBoxItem} className="p-2 rounded border">
            <div>
                <div className="flex justify-between items-center">
                    {/* 수신, 발신 구분 */}
                    <span className={`text-lg font-bold ${srcUser ? "text-red-400" : "text-blue-400"}`}>{srcUser || destUser || "이태희"}</span>
                    <span className="text-sm text-black/50">{date.toString().substring(0,10)}</span>
                </div>
                <div className="mt-6">{content}</div>
            </div>
        </div>
    );
};

const ChatBoxDetailItem = () =>{
    return (
        <div className="w-full p-4">
            <h3 className="text-lg">2</h3>
            <p className="mt-4 text-sm break-words">2</p>
        </div>
    );
}

const ChatBoxDetail = ({selectBoxItem}) =>{
    const detailItems = Array(10).fill(<ChatBoxDetailItem></ChatBoxDetailItem>);

    useEffect(()=>{
        console.log(selectBoxItem)
        selectBoxItem ?
        alert("Detail Item 조회 api 호출"):
        "";
    },[selectBoxItem])

    return (
        <section className="w-3/5 rounded-lg p-4 border flex flex-col">
            {/* 헤더 */}
            <div className="flex justify-between items-center">
                <h2>상대 유저 이름</h2>
                <div className="flex items-center gap-2">
                    <Button variant="ghost">
                       <Send className="mr-2 w-4 h-4"/>쪽지보내기
                    </Button>
                    <AlertDialog>
                        <AlertDialogTrigger asChild>
                            <Link>쪽지 삭제</Link>
                        </AlertDialogTrigger>
                        <AlertDialogContent>
                            <AlertDialogHeader>
                                <AlertDialogTitle>
                                    쪽지를 제거하시겠어요?
                                </AlertDialogTitle>
                                <AlertDialogDescription>
                                    내가 삭제하더라도 상대방은 쪽지를 볼 수 있어요.
                                </AlertDialogDescription>
                            </AlertDialogHeader>
                            <AlertDialogFooter>
                                <AlertDialogCancel>취소</AlertDialogCancel>
                                <AlertDialogAction onClick={() =>{alert("쪽지 삭제 api 호출")}}>삭제</AlertDialogAction>
                            </AlertDialogFooter>
                        </AlertDialogContent>
                    </AlertDialog>
                </div>
            </div>
            <div className="flex flex-col flex-grow overflow-auto gap-2">
                {
                    detailItems.map(item => item)
                }
            </div>
        </section>
    );
}

export default ChatBox;