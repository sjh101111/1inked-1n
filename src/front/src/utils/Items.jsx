import Article from "@/components/Article";
import { SelectItem } from "@/components/ui/select"
import { GenerateLiElUUID } from "./common";

/**
 * 
 * @param {{id: String, question: String}[]} questions 
 * @returns 
 */
export const getPasswordQuestionItems = (questions) =>{
    const questionSelectItems = questions.map(item => (<SelectItem key={item.id} value={item.id}>{item.question}</SelectItem>));
    return questionSelectItems;
}

/**
 * 
 * @param {{id: String, contents: String, createdAt: String,
 *  updatedAt: String, images: {id: String, img: Blob, articleId: String}[], 
 *  user: {realName: String, email: String, identity: String, location: String, description: String, image: Blob}, 
 * }[] articles 
 */
export const getArticleItems = (articles, afterDeleteFn) =>{
    const articleItems = articles.map(item => <Article key={item.id} {...item} afterDeleteFn={afterDeleteFn}></Article>);

    return articleItems;
}

/**
 * 
 * @param {{id: String, comments: String, createdAt: String, updatedAt: String, }[]} comments 
 */
export const getCommentItems = (comments) =>{

}

/**
 * 
 * @param {{id:String, img: Blob, articlId: String}[]} images 
 */
export const getArticlePictures = (images) => {
    return images.map(image => (
        <li key={image.id} className="w-[300px] h-[300px]">
            <img src={`data:image/png;base64,${image.img}`} className="w-full h-full object-cover" alt="Article Image"/>
        </li>
    ));
};