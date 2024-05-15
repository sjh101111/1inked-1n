import { Dialog, DialogClose, DialogContent, DialogFooter, DialogHeader, DialogTrigger } from "@/components/ui/dialog";
import { Textarea } from "@/components/ui/textarea";
import { Button } from "@/components/ui/button";
import {createChat} from "@/utils/API.js";
import {addChatReqParam} from "@/utils/Parameter.js";
import React, { useState } from 'react';

const ChatDialog = (props) =>{

    const resource = {
        btnText: "쪽지 보내기",
        clickCallback: async () => {
            const chatContent = document.getElementById('chatContent').value;

            if (!chatContent) {
                alert("메시지 내용을 입력해주세요.");
                return; // Early return if message is empty
            }

            try {
                // Call your API function
                const response = await createChat(addChatReqParam(props.partneremail, chatContent));

                if (response) { // Check for data in the successful response
                    alert("쪽지를 전송했습니다!");
                } else {
                    console.warn("Unexpected empty response from createChat"); // Log a warning for unexpected behavior
                    alert("메시지 전송에 문제가 발생했습니다. 다시 시도하십시오."); // Generic error message
                }
            } catch (error) {
                console.error("Error sending message:", error);
                alert("메시지 전송에 실패했습니다. 다시 시도하십시오."); // Error message
            }
        },
        initFn: () => {}
    }

    return (
        <Dialog>
            <DialogTrigger asChild {...props}></DialogTrigger>
            <DialogContent onPointerDownOutside={ev => ev.preventDefault()}>
                <DialogHeader>
                    <h1>쪽지 보내기</h1>
                </DialogHeader>
                <Textarea id="chatContent" className="mt-4 h-48 resize-none" placeholder="input your content"></Textarea>
                <DialogFooter>
                    <DialogClose asChild>
                        <Button className="bg-[#6866EB]" onClick={resource.clickCallback} type="submit">{resource.btnText}</Button>
                    </DialogClose>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
};

export default ChatDialog;








