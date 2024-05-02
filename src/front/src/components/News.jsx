import React from 'react';

const News = ({ title, description, originallink, pubDate })=> {
    return (
        <div>
            <h3>
                <a href={originallink} >{title}</a>
            </h3>
            <p>{description}</p>
            <p className = "opacity-70">{pubDate}</p>
        </div>
    );
};

export default News;