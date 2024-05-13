import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"

const FileInput = () =>{
    return (
        <div className="grid w-full items-center gap-1.5">
          <Label htmlFor="picture">첨부파일(shift, control키로 여러 파일 선택 가능)</Label>
          <Input id="picture" accept=".png" multiple type="file"/>
        </div>
    );
}

export default FileInput;