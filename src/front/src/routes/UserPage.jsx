import Header from "@/components/Layout/Header.jsx";
import {Avatar, AvatarImage, AvatarFallback} from "@/components/ui/avatar.jsx";
import {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {Tabs, TabsList, TabsTrigger, TabsContent} from "@/components/ui/tabs";
import { Button } from "@/components/ui/button";
import ChatDialog from "@/components/ChatDialog";
import { Send } from "lucide-react";
import { anchorScrollCallback } from "@/utils/common";

const UserPage = () => {
    const [profilePic, setProfilePic] = useState('');
    const [identity, setIdentity] = useState('Student');
    const [location, setLocation] = useState('Location');
    const [description, setDescription] = useState('Description');
    const [username, setUsername] = useState('Username');

    return (
        <>
            <Header></Header>
            <main className="flex flex-col items-center bg-gray-100 min-h-screen">
                <div className="w-3/5 p-5 bg-white shadow-lg rounded mt-6 overflow-hidden">
                    <Avatar className="w-40 h-40">
                        {profilePic ? (
                            <AvatarImage src={profilePic} alt="User profile picture"/>
                        ) : (
                            <AvatarFallback>{username.charAt(0)}</AvatarFallback>
                        )}
                    </Avatar>
                    <div className="mt-4 flex justify-between items-center">
                        <strong>{username}</strong>
                        <ChatDialog>
                            <Button variant="ghost">
                                <Send className="mr-2 w-4 h-4" /> 쪽지 보내기
                            </Button>
                        </ChatDialog> 
                    </div>
                    <div className="flex flex-col gap-4">
                        <p className="w-full mt-2">{identity}</p>
                        <p className="w-full">{location}</p>
                        <p className="w-full" style={{whiteSpace: 'pre-wrap'}}>{description}</p>
                    </div>
                </div>
                <Tabs defaultValue="account" className="w-3/5 mt-6">
                    <TabsList className="w-full flex">
                        <TabsTrigger asChild className="flex-grow" value="articles">
                            <a onClick={anchorScrollCallback} href="#articles">Articles</a>
                        </TabsTrigger>
                        <TabsTrigger className="flex-grow" value="comments">
                            <a onClick={anchorScrollCallback} href="#comments">Comments</a>
                        </TabsTrigger>
                        <TabsTrigger className="flex-grow" value="follwAndFollower">
                            <a onClick={anchorScrollCallback} href="#followAndFollwer">Follow/Follower</a>
                        </TabsTrigger>
                    </TabsList>
                    <TabsContent id="articles" className="w-full flex justify-center items-center" value="articles">Make changes to your account here.</TabsContent>
                    <TabsContent id="comments" className="w-full flex justify-center items-center" value="comments">Change your password here.</TabsContent>
                </Tabs>
            </main>
        </>
    );
};


export default UserPage;
