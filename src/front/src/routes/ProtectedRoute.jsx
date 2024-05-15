import { Navigate } from 'react-router-dom';
import {getAccessToken} from "@/utils/Cookie.js"; // 적절한 경로로 수정하세요.

const ProtectedRoute = ({ children }) => {
    const token = getAccessToken();

    // 토큰이 없다면 메인 페이지로 리다이렉트
    if (!token) {
        return <Navigate to="/" replace />;
    }

    // 토큰이 있다면 자식 컴포넌트 렌더링
    return children;
};

export default ProtectedRoute;