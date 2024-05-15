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
import {fetchLoginUserProfile, getResumeByUser, readComment, saveProfile} from "@/utils/API";
import Article from "@/components/Article.jsx";
import {readAllMyArticle} from "@/utils/API";
import {useUserInfo} from "@/utils/store.js";
import {getFollows, getFollowers} from "@/utils/API";


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
    const [file, setFile] = useState(null);
    const [activeTab, setActiveTab] = useState('articles');
    const {userInfo, setUserInfo} = useUserInfo();


    useEffect(() =>{
        setIdentity(userInfo.identity);
        setLocation(userInfo.location);
        setDescription(userInfo.description);
    },[]);

    const toggleEditing = () => {
        setEditing(!editing);
    };

    //프로필 변경 사항 저장 API
    const doSaveProfile = () =>{
        const reqParam = saveProfileReqParam(identity, location, description, file);

        if(!reqParam.file){
            alert('프로필로 사용할 파일을 업로드 해주세요.');
            return ;
        }

        saveProfile(reqParam)
        .then((response) => {
            const newUserInfo ={
                ...userInfo
            };

            newUserInfo.identity = identity;
            newUserInfo.location = location;
            newUserInfo.description = description;
            newUserInfo.profileSrc = profilePic;
            setUserInfo(newUserInfo);
        })
        .catch(() =>{
            alert("프로필 저장에 실패하였습니다.");
        })
        .finally(() =>{
            toggleEditing();
        });
        
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
                        {userInfo.profileSrc ? (
                            <>
                                <AvatarImage src={userInfo.profileSrc} alt="User profile picture"/>
                                <AvatarFallback></AvatarFallback>
                            </>
                            ) : (
                            <AvatarFallback></AvatarFallback>
                        )}
                    </Avatar>
                    <div className="mt-4 flex justify-between items-center">
                        <strong>{userInfo.realName}</strong>
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
                        <TabsTrigger className="flex-grow" value="followAndFollower">Follow/Follower</TabsTrigger>
                        <TabsTrigger className="flex-grow" value="myresumes">My Resume</TabsTrigger>
                    </TabsList>
                    <TabsContent id="articles" className="w-full" value="articles">
                        {activeTab === 'articles' && <ArticlesTab />}
                    </TabsContent>
                    <TabsContent id="followAndFollower" className="w-full" value="followAndFollower">
                        {activeTab === 'followAndFollower' && <FollowAndFollowerTab />}
                    </TabsContent>
                    <TabsContent id="myresumes" className="w-full" value="myresumes">
                        {activeTab === 'myresumes' && <ResumesTab />}
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

const ResumesTab = () => {
    const [resumes, setResumes] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchResumes = async () => {
            setLoading(true);
            try {
                const response = await getResumeByUser();
                if (Array.isArray(response)) {
                    // 'createdAt'을 기준으로 내림차순 정렬하여 가장 최근의 데이터가 배열의 첫 번째에 오도록 함
                    const sortedResumes = response.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
                    setResumes(sortedResumes);
                } else {
                    setResumes([]);
                }
            } catch (error) {
                console.error('Error reading resumes:', error);
            } finally {
                setLoading(false);
            }
        };
        fetchResumes();
    }, []);

    return (
        <div className="w-full">
            {loading ? (
                <p>Loading...</p>
            ) : (
                resumes.map((resume, index) => (
                    <MyResumes key={resume.id} resume={resume} order={resumes.length - index} />
                ))
            )}
        </div>
    );
};

const FollowAndFollowerTab = () => {
    const [follows, setFollows] = useState([]);
    const [followers, setFollowers] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchFollowAndFollower = async () => {
            setLoading(true);
            try {
                const followsData = await getFollows(); // Fetch follows data
                const followersData = await getFollowers(); // Fetch followers data
                setFollows(followsData); // Set follows state
                setFollowers(followersData); // Set followers state
            } catch (error) {
                console.error('Error fetching follow and follower data:', error);
                setFollows([]); // Set follows state to empty array on error
                setFollowers([]); // Set followers state to empty array on error
            } finally {
                setLoading(false); // Ensure loading is set to false after operation completes
            }
        };

        fetchFollowAndFollower(); // Initiate the fetch operation
    }, []);

    return (
        <div className="w-full">
            {loading ? (
                <p>Loading...</p>
            ) : (
                <FollowInfo follows={follows} followers={followers} />
            )}
        </div>
    );
};


export default MyPage;