import { Send } from "lucide-react";
import { Button } from "./ui/button";
import { Link } from "react-router-dom";
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger } from "./ui/alert-dialog";
import { useState, useEffect } from "react";
import ChatDialog from "./ChatDialog";
import {addChatReqParam} from "@/utils/Parameter.js";
import {createChat} from "@/utils/API.js";
import {readChatWithPartner} from "@/utils/API.js";
import {updateIsDeleted} from "@/utils/API.js";
import {deleteChat} from "@/utils/API.js";
import {readChatSummaries} from "@/utils/API.js";

const ChatBox = () => {
    const [selectBoxItem, setBoxItem] = useState(null);
    const [chatSummaries, setChatSummaries] = useState([]);

    useEffect(() => {
        readChatSummaries().then(data => {
            console.log('Received chat summaries:', data); // 받은 데이터 로깅
            if (Array.isArray(data)) {
                setChatSummaries(data.filter(summary => !summary.isDeleted));
            } else {
                console.error('Data is not an array:', data);
            }
        }).catch(console.error);
    }, []);
    return (
        <section className="chat p-5 flex h-[calc(100%-60px)] gap-10">
            <section className={`rounded-lg p-4 flex flex-col w-2/5 border`}>
                <h2 className="text-lg font-bold">쪽지함</h2>
                <div className="mt-4 flex flex-col overflow-auto flex-grow gap-4">
                    {chatSummaries.map(summary => (
                        <CheckBoxItem
                            key={summary.id}
                            srcUser={summary.me.realname}
                            destUser={summary.partner.realname}
                            partnerEmail={summary.partner.email}
                            date={new Date(summary.lastMessageAt)}
                            content={summary.lastMessage}
                            setBoxItem={() => setBoxItem({...summary, partnerEmail: summary.partner.email})}
                        />
                    ))}
                </div>
            </section>
            {selectBoxItem && <ChatBoxDetail selectBoxItem={selectBoxItem} />}
        </section>
    );
};

const CheckBoxItem = ({srcUser="", destUser="", date=new Date(), content="lorem", setBoxItem}) =>{
    return (
        <div onClick={setBoxItem} className="p-2 rounded border">
            <div>
                <div className="flex justify-between items-center">
                    {/* 수신, 발신 구분 */}
                    <span className="text-lg font-bold text-blue-400">{destUser || "이태희"}</span>
                    <span className="text-sm text-black/50">{date.toString().substring(0, 10)}</span>
                </div>
                <div className="mt-6">{content}</div>
            </div>
        </div>
    );
};

const ChatBoxDetailItem = ({message}) =>{
    console.log("Message Data:", message);
    const isSentByMe = message.me.id === message.sendUser.id;
    const messageType = isSentByMe ? "보낸 쪽지" : "받은 쪽지";

    // 클래스 조건부 결정
    const messageAlignment = isSentByMe ? "justify-end" : "justify-start";

    return (
        <div className={`w-full p-4 flex ${messageAlignment}`}>
            <div className="bg-gray-100 rounded-lg p-2 max-w-xs md:max-w-md">
                <p className="text-xs text-gray-500">{messageType}</p> {/* 메시지 유형 표시 */}
                <p className="text-sm break-words">{message.contents}</p> {/* 메시지 내용 */}
            </div>
        </div>
    );
}

const ChatBoxDetail = ({selectBoxItem}) => {
    const [detailMessages, setDetailMessages] = useState([]);

    useEffect(() => {
        if (selectBoxItem) {

            readChatWithPartner(selectBoxItem.partnerEmail)
                .then(data => { // 이제 'data'는 실제 응답된 데이터입니다.
                    console.log("Received Data:", data);
                    setDetailMessages(data.reverse()); // 'data'를 직접 사용합니다.
                    console.log("Updated Messages State:", data);
                })
                .catch(error => console.error("Fetching chat details failed", error));
        }
    }, [selectBoxItem]);

    const handleMessageSent = async () => {
        // 새로운 메시지를 받아옵니다.
        try {
            const data = await readChatWithPartner(selectBoxItem.partnerEmail);
            console.log("Received New Message:", data);
            setDetailMessages(data.reverse());
        } catch (error) {
            console.error("Error fetching new message:", error);
        }
    };

    const handleDeleteChat = async () => {
        if (detailMessages.length > 0 && detailMessages[0].partner && detailMessages[0].partner.email) {
            try {
                await updateIsDeleted(detailMessages[0].partner.email);
                alert('쪽지가 삭제되었습니다.');
                // Optionally, update the state or UI here, such as clearing the deleted chat
                setDetailMessages([]);
            } catch (error) {
                alert('삭제 실패: ' + error.message);
            }
        } else {
            alert('상대방 이메일이 없어 쪽지를 삭제할 수 없습니다.');
        }
    };

    return (
        <section className="w-3/5 rounded-lg p-4 border flex flex-col">
            {/* 헤더 */}
            <div className="flex justify-between items-center">
                <h2>{detailMessages.length > 0 && detailMessages[0].partner && detailMessages[0].partner.realname ? detailMessages[0].partner.realname : 'Loading...'}</h2>
                <div className="flex items-center gap-2">
                    <ChatDialog partneremail={detailMessages.length > 0 && detailMessages[0].partner && detailMessages[0].partner.email ? detailMessages[0].partner.email : 'Loading...'}
                                onMessageSent={handleMessageSent}>
                        <Button variant="ghost">
                            <Send className="mr-2 w-4 h-4"/>쪽지보내기
                        </Button>
                    </ChatDialog>
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
                                <AlertDialogAction onClick={() =>{handleDeleteChat()}}>삭제</AlertDialogAction>
                            </AlertDialogFooter>
                        </AlertDialogContent>
                    </AlertDialog>
                </div>
            </div>
            {/* detailMessages가 비어있지 않은 경우에만 렌더링 */}
            {detailMessages && detailMessages.length > 0 && (
                <div className="flex flex-col flex-grow overflow-auto gap-2">
                    {detailMessages.map((msg) => (
                        <ChatBoxDetailItem key={msg.id} message={msg} />
                    ))}
                </div>
            )}
        </section>
    );
}

export default ChatBox;