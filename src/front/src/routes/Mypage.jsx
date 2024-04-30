import Header from "@/components/Layout/Header.jsx";
import {Avatar, AvatarImage, AvatarFallback} from "@/components/ui/avatar.jsx";
import {useState} from "react";
import {Link} from "react-router-dom";
import {Tabs, TabsList, TabsTrigger, TabsContent} from "@/components/ui/tabs";

const Mypage = () => {
    const [editing, setEditing] = useState(false);
    const [profilePic, setProfilePic] = useState('');
    const [identity, setIdentity] = useState('Student');
    const [location, setLocation] = useState('Location');
    const [description, setDescription] = useState('Description');
    const [username, setUsername] = useState('Username');

    const toggleEditing = () => {
        setEditing(!editing);
    };

    const handleProfilePicChange = (event) => {
        const file = event.target.files[0];
        const reader = new FileReader();
        reader.onloadend = () => {
            setProfilePic(reader.result);
        };
        reader.readAsDataURL(file);
    };

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
                    <div>
                        <strong>{username}</strong>
                        <Link to="/findPassword" className="text-black/65 float-right">비밀번호 변경 및 회원 탈퇴</Link>
                    </div>
                    <div>
                        {editing ? (
                            <input className="w-96" maxLength={100} type="text" value={identity} onChange={e => setIdentity(e.target.value)}/>
                        ) : (
                            <p>{identity}</p>
                        )}
                    </div>
                    <div>
                        {editing ? (
                            <input className="w-96" maxLength={50} type="text" value={location} onChange={e => setLocation(e.target.value)}/>
                        ) : (
                            <p>{location}</p>
                        )}
                    </div>
                    <div>
                        {editing ? (
                            <textarea className="w-96" maxLength={2000} value={description} onChange={e => setDescription(e.target.value)}/>
                        ) : (
                            <p style={{whiteSpace: 'pre-wrap'}}>{description}</p>
                        )}
                    </div>
                    {editing && (
                        <input type="file" onChange={handleProfilePicChange}/>
                    )}
                    <button onClick={toggleEditing} className="text-black text-opacity-40">
                        {editing ? 'Save Changes' : 'Edit'}
                    </button>
                </div>
                <Tabs defaultValue="account" className="w-[400px] mt-6">
                    <TabsList>
                        <TabsTrigger value="articles">Articles</TabsTrigger>
                        <TabsTrigger value="Comments">Comments</TabsTrigger>
                        <TabsTrigger value="follwAndFollower">Follow/Follower</TabsTrigger>
                        <TabsTrigger value="myresume">My Resume</TabsTrigger>
                    </TabsList>
                    <TabsContent value="articles">Make changes to your account here.</TabsContent>
                    <TabsContent value="password">Change your password here.</TabsContent>
                </Tabs>
            </main>
        </>
    );
};


export default Mypage;
