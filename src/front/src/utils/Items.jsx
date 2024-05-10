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