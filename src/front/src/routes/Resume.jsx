import Header from "@/components/Layout/Header.jsx";
import {Textarea} from "@/components/ui/textarea.jsx";
import {Button} from "@/components/ui/button.jsx";
import React, {useEffect, useState} from 'react';
import {getReviewedResumeByAllenAI} from "@/utils/API.js";
import {addResumeReqParam} from "@/utils/Parameter.js";
import {saveResume} from "@/utils/API.js";

const Resume = () => {
    const [content, setContent] = useState(''); // 자소서 내용 입력 상태
    const [editRequest, setEditRequest] = useState(''); // 첨삭 요청 내용 입력 상태
    const [analysisResult, setAnalysisResult] = useState(''); // AI 분석 결과 상태
    const [loading, setLoading] = useState(false); // 로딩 상태 추가
    const client_id = "8c9cad6a-45a6-4129-a938-0db6b017899d"; // 클라이언트 ID 선언
    const promptMessage =
        "1. Make this contents more better resume like native english person writes" +
        "2. This is used for application at good company or university, so Keep your great tone and manner"+
        "3. Write resume in a logical and easy-to-read bracket"

    // API 호출을 처리할 이벤트 핸들러
    const handleAnalyze = () => {
        const combinedContent = `${content} | ${editRequest} | ${promptMessage}`;
        setLoading(true); // 로딩 시작
        getReviewedResumeByAllenAI(client_id, combinedContent)
            .then(data => {
                setAnalysisResult(data); // 분석 결과 상태 업데이트
                setLoading(false); // 로딩 종료
            })
            .catch(error => {
                console.error('Error:', error); // 에러 로깅
                setLoading(false); // 로딩 종료
            });
    };

    const handleEnglishInput = (event, setValue) => {
        const { value } = event.target;
        // 정규 표현식을 사용하여 입력 값이 영어 알파벳, 공백, 특수 문자만 포함되어 있는지 검사
        if (/^[a-zA-Z\s.,'!?-]*$/.test(value)) {
            setValue(value);
        } else {
            // 영어가 아닌 문자가 입력되면 경고 메시지 출력 (옵션)
            alert("Only English characters are allowed.");
        }
    };

    const saveResumeHandler = () => {
        if (!analysisResult.content) {
            alert('저장할 내용이 없습니다.');
            return;
        }
        // 예시: localStorage에 저장하는 로직
        if (analysisResult.content) {
            saveResume(addResumeReqParam(analysisResult.content)).then(response => {
                console.log('저장 성공:', response);
                alert('Resume 저장 완료');
            }).catch(
               error => console.error('Error:', error)
            )
        }
    }

    return (
        <>
            <Header/>
            <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'flex-start',
                width: '1360px',
                margin: '0 auto',
                gap: '30px',
                marginTop: '10px'
            }}>
                <section style={{width: '680px'}}>
                    <label htmlFor="resumeContent">Resume 입력</label>
                    <Textarea id="resumeContent"
                              onChange={(ev) => handleEnglishInput(ev, setContent)}
                              className="mt-4 resize-none"
                              style={{height: '450px', overflowY: 'auto'}}
                              maxLength={900}
                              placeholder="첨삭 받고 싶은 Resume를 입력해주세요"
                              value={content}

                    />
                    <label htmlFor="requirement" style={{display: 'block', marginTop: '10px'}}>추가 입력란</label>
                    <Textarea id="requirement"
                              onChange={(ev) => handleEnglishInput(ev, setEditRequest)}
                              className="mt-4 resize-none"
                              style={{height: '100px', overflowY: 'auto'}}
                              maxLength={100}
                              placeholder="어떻게 첨삭받고 싶은지 입력해주세요"
                              value={editRequest}
                    />
                    <Button className="bg-[#6866EB] w-48 hover:bg-violet-600" onClick={handleAnalyze}
                            style={{marginTop: '10px', float: 'right'}}>
                        첨삭하기
                    </Button>
                </section>
                <section style={{width: '680px'}}>
                    <label htmlFor="aiReviewdResume">AI 첨삭 Resume</label>
                    {loading ? (
                        <div id="aiReviewdResume"
                             className="mt-4 p-2 resize-none border border-gray-300 rounded"
                             style={{height: '600px', whiteSpace: 'pre-wrap', overflowY: 'auto'}}>
                        <p>Loading...</p>
                        </div>
                    ) : (
                        <div id="aiReviewdResume"
                             className="mt-4 p-2 resize-none border border-gray-300 rounded"
                             style={{height: '600px', whiteSpace: 'pre-wrap', overflowY: 'auto'}}>
                            {analysisResult.content || "AI 분석 결과가 여기에 표시됩니다."}
                        </div>
                    )}
                    <Button className="bg-[#6866EB] w-48 hover:bg-violet-600" onClick={saveResumeHandler}
                            style={{marginTop: '10px', float: 'right'}}>
                        저장하기
                    </Button>
                </section>
            </div>
        </>
    );
};

export default Resume;
