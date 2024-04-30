import { Button } from "./ui/button";

const ChatBox = (props) =>{
    const tempChkBoxItems = Array(10).fill(<CheckBoxItem></CheckBoxItem>);
    return (
        <section className="chat p-5 flex h-[calc(100%-60px)] gap-10">
            <section className={`rounded-lg p-4 flex flex-col w-2/5`}>
                <h2 className="text-lg font-bold">쪽지함</h2>
                <div className="mt-4 flex flex-col overflow-auto flex-grow gap-4">
                   {
                    tempChkBoxItems.map(item => item)
                   } 
                </div>
            </section>

            <ChatBoxDetail></ChatBoxDetail>
        </section>
    );
};

const CheckBoxItem = ({srcUser="", destUser="", date=new Date(), content="lorem"}) =>{
    return (
        <div className="p-2 rounded border">
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

const ChatBoxDetail = () =>{
    return (
        <section className="w-3/5 rounded-lg p-4 bg-indigo-200">
            <div className="flex justify-between items-center">
                <h2>상대 유저 이름</h2>
                <div>
                    <Button>쪽지보내기</Button>
                    <Button>쪽지 삭제</Button>
                </div>
            </div>
        </section>
    );
}

export default ChatBox;