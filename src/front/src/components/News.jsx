import React from 'react';

const News = ({ title, description, link})=> {
    return (
        <div className="flex flex-col border border-grey rounded-md ">
            <h1 className ="text-lg font-medium text-[#6866EB]"><a href ={link}> {title}  </a></h1>
            <p  className = "font-light" dangerouslySetInnerHTML={{ __html:description}}></p>

        </div>
    );
};

export default News;