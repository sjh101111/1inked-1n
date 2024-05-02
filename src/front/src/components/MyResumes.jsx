import { Table, TableBody, TableCaption, TableCell, TableHead, TableHeader,TableRow } from "@/components/ui/table"
import { Button } from "./ui/button";
import { Link } from "react-router-dom";
import { GenerateLiElUUID } from "@/utils/common";
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTrigger } from "@/components/ui/dialog";
const MyResumes = () =>{
    const getTableItem = (ResumeInfo, order = 0, key) =>{
        return (
            <TableRow key={key}>
                <TableCell className="font-medium text-center">{order}</TableCell>
                <TableCell>{ResumeInfo.name}</TableCell>
                <TableCell className="text-right">
                    <Dialog>
                        <DialogTrigger asChild>
                            <Button size="sm" variant="outline">
                                확인하기
                            </Button>
                        </DialogTrigger>
                        <DialogContent>
                            <DialogHeader>
                                <h2 className="text-lg font-bold">{ResumeInfo.name}</h2>
                            </DialogHeader>
                            <div className="p-2">
                                <span>
                                    {ResumeInfo.content}
                                </span>
                            </div>
                        </DialogContent>
                    </Dialog>
                    
                </TableCell>
            </TableRow>
        );
    };

    let dummyTableItems = Array(10).fill();
    dummyTableItems = dummyTableItems.map(i => getTableItem({name: "temp", content: "hihi"},0,GenerateLiElUUID()))

    return (
        //스크롤 이벤트 처리 위한 mt-20
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
                                <TableHead>Name</TableHead>
                                <TableHead className="text-right">Actions</TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            {
                                dummyTableItems.map(item => item)
                            }
                        </TableBody>
                    </Table>
                </div>
            </div>
            </section>
    );
};

export default MyResumes;