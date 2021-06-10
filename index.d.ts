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
  /**
   * 打开文件
   * @param para 参数 需注意 ios需要使用数组
   * @param cb  打开文件的回调
   */
  export function openDoc(para: DocProps, cb: Function): void;
  export function openDocb64(para: Doc64Props, cb: Function): void;
  export function openDocBinaryinUrl(para: DocProps, cb: Function): void;
}
