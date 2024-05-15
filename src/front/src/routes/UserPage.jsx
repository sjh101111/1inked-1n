import Header from "@/components/Layout/Header.jsx";
import {Avatar, AvatarImage, AvatarFallback} from "@/components/ui/avatar.jsx";
import {useEffect, useState} from "react";
import {Link, redirect, useLocation, useNavigate} from "react-router-dom";
import {Tabs, TabsList, TabsTrigger, TabsContent} from "@/components/ui/tabs";
import {Button} from "@/components/ui/button";
import ChatDialog from "@/components/ChatDialog";
import { useFollowee, useFollower } from "@/utils/store";
import {Send} from "lucide-react";
import { followUserReqParam } from "@/utils/Parameter";
import {
    followUser,
    unFollowUser,
    getFollowers,
    getFollows,
    fetchAnotherUserProfile,
    getFollowersOfUser,
    getFollowsOfUser, getResumeByEmail,
    readChatWithPartner, readUserAllArticles
} from "@/utils/API";
import {getAccessTokenInfo} from "@/utils/Cookie.js"
import Article from "@/components/Article.jsx";
import MyResumes from "@/components/MyResumes.jsx";
import FollowInfo from "@/components/FollowInfo.jsx";

const UserPage = () => {
    const nevigate = useNavigate();

    //글로벌 컨텍스트
    const {followeeInfo, setFolloweeInfo} = useFollowee();
    const {followerInfo, setFollowerInfo} = useFollower();

    //route 변경시, 값 전달위한 파라미터
    const {state} = useLocation();
    const navigate = useNavigate();
    const [profilePic, setProfilePic] = useState('');
    const [identity, setIdentity] = useState('Student');
    const [location, setLocation] = useState('Location');
    const [description, setDescription] = useState('Description');
    const [username, setUsername] = useState('Username');
    const [userId, setUserId] = useState('');
    const [isFollow, setFollow] = useState(false);
    const [activeTab, setActiveTab] = useState('');

    useEffect(() =>{
        const queryEmail = state?.email || "test@test.com";
        const accessToken = getAccessTokenInfo();

        if (accessToken.sub === queryEmail) {
            navigate("/mypage");
        }

        fetchAnotherUserProfile(queryEmail)
        .then((userInfo) =>{
            setUserId(userInfo.id);
            setUsername(userInfo.realName);
            setIdentity(userInfo.identity);
            setLocation(userInfo.location);
            setDescription(userInfo.description);

            setProfilePic(`data:image/png;base64,${userInfo.image}`);

            getFollowers()
            .then((follwers) =>{
                setFollowerInfo(follwers);

                getFollows()
                .then((followee) =>{
                    setFolloweeInfo(followee);
                    setFollow(followee.filter(user =>{ return queryEmail === user.email}).length != 0);
                });
            });
        });
    },[state?.email, nevigate]);

    const handleFollow = () =>{
        const reqParam = followUserReqParam(userId);

        followUser(reqParam)
        .then((data) =>{
            setFollow(true);
        })
    }

    const handleUnFollow = () =>{
        unFollowUser(userId)
        .then((data) =>{
            setFollow(false);
        })
    }

    return (
        <>
            <Header></Header>
            <main className="flex flex-col items-center bg-gray-100 min-h-screen">
                <div className="w-3/5 p-5 bg-white shadow-lg rounded mt-6 overflow-hidden">
                    <Avatar className="w-40 h-40">
                        {profilePic ? (
                            <>
                                <AvatarImage src={profilePic} alt="User profile picture"/>
                                <AvatarFallback>{username?.charAt(0)}</AvatarFallback>
                            </>
                        ) : (
                            <AvatarFallback>{username?.charAt(0)}</AvatarFallback>
                        )}
                    </Avatar>
                    <div className="mt-4 flex justify-between items-center">
                        <strong>{username}</strong>
                        <div className="flex gap-2">
                            {
                                !isFollow ?
                                <Button className="text-sm text-black/65 bg-[#6866EB] hover:bg-violet-600" onClick={handleFollow} variant="ghost">FOLLOW</Button> :
                                <Button className="text-sm text-black/65 bg-slate-400 hover:bg-slate-300" onClick={handleUnFollow} variant="ghost">UNFOLLOW</Button>
                            }
                            <ChatDialog partneremail={state?.email}>
                                <Button variant="ghost">
                                    <Send className="mr-2 w-4 h-4"/> 쪽지 보내기
                                </Button>
                            </ChatDialog>
                        </div>
                    </div>
                    <div className="flex flex-col gap-4">
                        <p className="w-full mt-2">{identity || "Identity 정보가 없습니다."}</p>
                        <p className="w-full">{location || "Location 정보가 없습니다."}</p>
                        <p className="w-full"
                           style={{whiteSpace: 'pre-wrap'}}>{description || "Description 정보가 없습니다."}</p>
                    </div>
                </div>
                <Tabs defaultValue="" className="w-3/5 mt-6" onValueChange={setActiveTab}>
                    <TabsList className="w-full flex">
                        <TabsTrigger className="flex-grow" value="articles">Articles</TabsTrigger>
                        <TabsTrigger className="flex-grow" value="followAndFollower">Follow/Follower</TabsTrigger>
                        <TabsTrigger className="flex-grow" value="myresumes">My Resume</TabsTrigger>
                    </TabsList>
                    <TabsContent id="articles" className="w-full" value="articles">
                        {activeTab === 'articles' && <ArticlesTab email={state?.email}/>}
                    </TabsContent>
                    <TabsContent id="followAndFollower" className="w-full" value="followAndFollower">
                        {activeTab === 'followAndFollower' && <FollowAndFollowerTab email={state?.email}/>}
                    </TabsContent>
                    <TabsContent id="myresumes" className="w-full" value="myresumes">
                        {activeTab === 'myresumes' && <ResumesTab email={state?.email}/>}
                    </TabsContent>
                </Tabs>
            </main>
        </>
    );
};

const ArticlesTab = ({email}) => {
    const [articles, setArticles] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchArticles = async () => {
            setLoading(true);
            try {
                const response = await readUserAllArticles(email);
                if (response && Array.isArray(response) && response.length > 0) {
                    setArticles(response);
                } else {
                    // 데이터가 없거나 응답 형식이 예상과 다른 경우 빈 배열을 설정
                    setArticles([]);
                }
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
                    <Article key={article.id} {...article}
                             afterDeleteFn={(id) => setArticles(prev => prev.filter(a => a.id !== id))}/>
                ))
            )}
        </div>
    );
};

const ResumesTab = ({email}) => {
    const [resumes, setResumes] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchResumes = async () => {
            setLoading(true);
            try {
                const response = await getResumeByEmail(email);
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
                    <MyResumes key={resume.id} resume={resume} order={resumes.length - index}/>
                ))
            )}
        </div>
    );
};

const FollowAndFollowerTab = ({email}) => {
    const [follows, setFollows] = useState([]);
    const [followers, setFollowers] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchFollowAndFollower = async () => {
            setLoading(true);
            try {
                const followsData = await getFollowsOfUser(email); // Fetch follows data
                const followersData = await getFollowersOfUser(email); // Fetch followers data
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
                <FollowInfo follows={follows} followers={followers}/>
            )}
        </div>
    );
};

export default UserPage;
