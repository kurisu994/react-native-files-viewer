declare module "react-native-files-viewer" {
  interface DocProps {
    url: String;
    fileName: String;
    fileType: "pdf" | "png" | "jpg" | "xls" | "doc" | "ppt" | "xlsx" | "docx" | " pptx";
    cache: boolean;
    /**ios only */
    fileNameOptional?: string;
  }

  interface Doc64Props {
    base64: String;
    fileName: String;
    fileType: "pdf" | "png" | "jpg" | "xls" | "doc" | "ppt" | "xlsx" | "docx" | " pptx";
    cache: boolean;
    /**ios only */
    fileNameOptional?: string;
  }

  enum ErrorCode {
    UNKNOWN_ERROR = 1,
    /** file not found */
    FILE_NOT_FOUND = 2,
    /** incomplete parameters */
    INCOMPLETE_PARAMETERS = 3
  }

  type ICallback = (err?: ErrorCode, fileName?: string) => void;
  /**
   * 打开文件
   * @param para 参数 需注意 ios需要使用数组
   * @param cb  打开文件的回调
   */
  export function openDoc(para: DocProps, cb: ICallback): void;
  export function openDocb64(para: Doc64Props, cb: ICallback): void;
  export function openDocBinaryinUrl(para: DocProps, cb: ICallback): void;
}
