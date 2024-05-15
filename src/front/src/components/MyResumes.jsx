import { Table, TableBody, TableCaption, TableCell, TableHead, TableHeader,TableRow } from "@/components/ui/table"
import { Button } from "./ui/button";
import { Link } from "react-router-dom";
import { GenerateLiElUUID } from "@/utils/common";
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTrigger } from "@/components/ui/dialog";
const MyResumes = ({ resume, order }) =>{
    const { id, contents, createdAt, updatedAt } = resume;
    const getTableItem = (ResumeInfo, order = 0, key) =>{
        return (
            <TableRow key={key}>
                <TableCell className="font-medium text-center">{order}</TableCell>
                <TableCell>{ResumeInfo.contents}</TableCell>
                <TableCell className="text-right">
                    <Dialog>
                        <DialogTrigger asChild>
                            <Button size="sm" variant="outline">
                                확인하기
                            </Button>
                        </DialogTrigger>
                        <DialogContent>
                            <DialogHeader>
                                <h2 className="text-lg font-bold">{ResumeInfo.contents}</h2>
                            </DialogHeader>
                            <div className="p-2">
                                <span>{ResumeInfo.contents}</span>
                            </div>
                        </DialogContent>
                    </Dialog>
                </TableCell>
            </TableRow>
        );
    };

    return (
        <section className="container mx-auto mt-20">
            <div className="flex flex-col gap-6 md:gap-8 lg:gap-10">
                <div className="flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
                    <div className="grid gap-2">
                        <h2 className="text-2xl font-bold tracking-tight">내 첨삭 Resume</h2>
                        <p className="text-gray-500 dark:text-gray-400">AI 엘런이 첨삭한 Resume를 다시 확인할 수 있습니다!</p>
                    </div>
                </div>
                <div className="overflow-x-auto">
                    <Table>
                        <TableHeader>
                            <TableRow>
                                <TableHead className="w-[100px]">Order</TableHead>
                                <TableHead>Contents</TableHead>
                                <TableHead className="text-right">Actions</TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            {getTableItem({ id, contents, createdAt, updatedAt }, order, GenerateLiElUUID())}
                        </TableBody>
                    </Table>
                </div>
            </div>
        </section>
    );
};


export default MyResumes;