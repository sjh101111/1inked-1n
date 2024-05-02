import Header from "@/components/Layout/Header.jsx";
import {Textarea} from "@/components/ui/textarea.jsx";
import {Button} from "@/components/ui/button.jsx";
import React, {useState} from 'react';

const Resume = () => {
    const [content, setContent] = useState(''); // 자소서 내용 입력 상태
    const [editRequest, setEditRequest] = useState(''); // 첨삭 요청 내용 입력 상태
    const [analysisResult, setAnalysisResult] = useState(''); // AI 분석 결과 상태

    const handleAnalyze = () => {
        // API 호출 로직 구현, 예시:
        fetch('API_ENDPOINT', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({content, editRequest}),
        })
            .then(response => response.json())
            .then(data => setAnalysisResult(data.analysis)) // AI 분석 결과를 상태로 저장
            .catch(error => console.error('Error:', error));
    };

    return (
        <>
            <Header />
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', width: '1360px', margin: '0 auto', gap: '30px',  marginTop:'10px' }}>
                <section style={{ width: '680px' }}>
                    <label htmlFor="resumeContent">Resume 입력</label>
                    <Textarea id="resumeContent"
                              onChange={(ev) => setContent(ev.target.value)}
                              className="mt-4 resize-none"
                              style={{ height: '450px', overflowY: 'auto'}}
                              maxLength={900}
                              placeholder="첨삭 받고 싶은 Resume를 입력해주세요"
                              value={content}

                    />
                    <label htmlFor="requirement" style={{ display: 'block',marginTop:'10px'}}>추가 입력란</label>
                    <Textarea id="requirement"
                              onChange={(ev) => setEditRequest(ev.target.value)}
                              className="mt-4 resize-none"
                              style={{ height: '100px', overflowY: 'auto'}}
                              maxLength={100}
                              placeholder="어떻게 첨삭받고 싶은지 입력해주세요"
                              value={editRequest}
                    />
                    <Button className="bg-[#6866EB] w-48 hover:bg-violet-600" onClick={handleAnalyze}
                            style={{ marginTop:'10px', float:'right'}}>
                        첨삭하기
                    </Button>
                </section>
                <section style={{ width: '680px' }}>
                    <label htmlFor="aiReviewdResume">AI 첨삭 Resume</label>
                    <div id="aiReviewdResume"
                         className="mt-4 p-2 resize-none border border-gray-300 rounded"
                         style={{ height: '600px', whiteSpace: 'pre-wrap', overflowY: 'auto' }}>
                        {analysisResult || "AI 분석 결과가 여기에 표시됩니다."}
                    </div>
                    <Button className="bg-[#6866EB] w-48 hover:bg-violet-600" onClick={() => { }}
                            style={{ marginTop:'10px', float:'right'}}>
                        저장하기
                    </Button>
                </section>
            </div>
        </>
    );
};

export default Resume;
