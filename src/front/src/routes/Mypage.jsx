import Header from "@/components/Layout/Header.jsx";
import {Avatar, AvatarImage, AvatarFallback} from "@/components/ui/avatar.jsx";
import {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {Tabs, TabsList, TabsTrigger, TabsContent} from "@/components/ui/tabs";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Button } from "@/components/ui/button";
import FollowInfo from "@/components/FollowInfo";
import MyResumes from "@/components/MyResumes";
import { anchorScrollCallback } from "@/utils/common";
import { saveProfileReqParam } from "@/utils/Parameter";
import {fetchLoginUserProfile, readComment, saveProfile} from "@/utils/API";
import Article from "@/components/Article.jsx";
import {readAllMyArticle} from "@/utils/API";

const MyPage = () => {
    //Mypage, UserPage는 유저 정보를 통해 pageOwner인지 판단한 후 구별이 가능
    //문제점
    //1. header에서 동일 page 접근하려했을때, 어떻게 적용되지?
    //동일 url을 클릭했을 때, 화면 전환이 안됨
    const [editing, setEditing] = useState(false);
    const [profilePic, setProfilePic] = useState('');
    const [identity, setIdentity] = useState('Student');
    const [location, setLocation] = useState('Location');
    const [description, setDescription] = useState('Description');
    const [username, setUsername] = useState('Username');
    const [file, setFile] = useState(null);
    const [activeTab, setActiveTab] = useState('articles');

    useEffect(() =>{
        fetchLoginUserProfile()
        .then(async (userInfo) => {
            setUsername(userInfo.realName);
            setIdentity(userInfo.identity);
            setLocation(userInfo.location);
            setDescription(userInfo.description);

            //user Profile image 설정
            setProfilePic(`data:image/png;base64,${userInfo.image}`);
        });
    },[]);

    const toggleEditing = () => {
        setEditing(!editing);
    };

    //프로필 변경 사항 저장 API
    const doSaveProfile = () =>{
        const reqParam = saveProfileReqParam(identity, location, description, file);

        saveProfile(reqParam)
        .then((response) => {
            console.log(response);
        })
        .finally(() =>{
            toggleEditing();
        });
        history.push("/mypage");
        
    }

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
                            <>
                                <AvatarImage src={profilePic} alt="User profile picture"/>
                                <AvatarFallback></AvatarFallback>
                            </>
                            ) : (
                            <AvatarFallback></AvatarFallback>
                        )}
                    </Avatar>
                    <div className="mt-4 flex justify-between items-center">
                        <strong>{username}</strong>
                        <div className="flex gap-2 text-black/65">
                            <Link to="/findPassword">비밀번호 변경</Link>
                            <Link to="/resign">회원 탈퇴</Link>
                        </div>
                    </div>
                    <div className="flex flex-col gap-4">
                            {
                                editing ?
                                (<>
                                    <Input className="w-full mt-2" maxLength={100} type="text" value={identity} onChange={e => setIdentity(e.target.value)}/>
                                    <Input className="w-full" maxLength={50} type="text" value={location} onChange={e => setLocation(e.target.value)}/>
                                    <Textarea className="w-full resize-none" maxLength={2000} value={description} onChange={e => setDescription(e.target.value)}/>
                                    <Input type="file" accept=".png" onChange={(ev) => { handleProfilePicChange(ev); setFile(ev.target.files[0])}}/>
                                    <Button onClick={doSaveProfile} variant="ghost" className="text-black text-opacity-40">
                                        Save Changes
                                    </Button>
                                </>) :
                                (<>
                                    <p className="w-full mt-2">{identity}</p>
                                    <p className="w-full">{location}</p>
                                    <p className="w-full" style={{whiteSpace: 'pre-wrap'}}>{description}</p>
                                    <Button onClick={toggleEditing} variant="ghost" className="text-black text-opacity-40">
                                        Edit
                                    </Button>
                                </>)
                            }
                    </div>
                </div>
                <Tabs defaultValue="articles" className="w-3/5 mt-6" onValueChange={setActiveTab}>
                    <TabsList className="w-full flex">
                        <TabsTrigger className="flex-grow" value="articles">Articles</TabsTrigger>
                        <TabsTrigger className="flex-grow" value="comments">Comments</TabsTrigger>
                        <TabsTrigger className="flex-grow" value="followAndFollower">Follow/Follower</TabsTrigger>
                        <TabsTrigger className="flex-grow" value="myresume">My Resume</TabsTrigger>
                    </TabsList>
                    <TabsContent id="articles" className="w-full" value="articles">
                        {activeTab === 'articles' && <ArticlesTab />}
                    </TabsContent>
                    <TabsContent id="comments" className="w-full flex justify-center items-center" value="comments">
                        Change your password here.
                    </TabsContent>
                    <TabsContent id="followAndFollower" className="w-full" value="followAndFollower">
                        <FollowInfo />
                    </TabsContent>
                    <TabsContent id="myresume" className="w-full" value="myresume">
                        <MyResumes />
                    </TabsContent>
                </Tabs>
            </main>
        </>
    );
};

const ArticlesTab = () => {
    const [articles, setArticles] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchArticles = async () => {
            setLoading(true);
            try {
                const response = await readAllMyArticle();
                setArticles(response);
            } catch (error) {
                console.error('Error reading articles: ', error);
            } finally {
                setLoading(false);
            }
        };
        fetchArticles();
    }, []);

    return (
        <div className="w-full">
            {loading ? (
                <p>Loading...</p>
            ) : (
                articles.map(article => (
                    <Article key={article.id} {...article} afterDeleteFn={(id) => setArticles(prev => prev.filter(a => a.id !== id))} />
                ))
            )}
        </div>
    );
};

export default MyPage;