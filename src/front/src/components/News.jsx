import React from 'react';

const News = ({ title, description, link})=> {
    return (
        <div className="flex flex-col border border-grey rounded-md w-full p-2">
            <h1 className ="text-lg font-medium text-[#6866EB]"><a href ={link} target="_blank"> {title}  </a></h1>
            <p  className = "font-light" dangerouslySetInnerHTML={{ __html:description}}></p>

        </div>
    );
};

export default News;