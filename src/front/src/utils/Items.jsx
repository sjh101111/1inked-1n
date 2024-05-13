import Article from "@/components/Article";
import { SelectItem } from "@/components/ui/select"

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
 * @param {{id: String, content: String, createdAt: String,
 *  updatedAt: String, images: {id: String, img: Blob, articleId: String}[], 
 *  user: {realName: String, email: String, identity: String, location: String, description: String, image: Blob}, 
 * }[] articles 
 */
export const getArticleItems = (articles) =>{
    const articleItems = articles.map(item => <Article key={item.id} {...item}></Article>);

    return articleItems;
}

/**
 * 
 * @param {{id: String, comments: String, createdAt: String, updatedAt: String, }[]} comments 
 */
export const getCommentItems = (comments) =>{

}