
import { NativeModules } from 'react-native';
const RNFileViewerModule = NativeModules.RNFileViewerModule;
export default {
  openDoc: RNFileViewerModule.openDoc,
  openDocb64: RNFileViewerModule.openDocb64,
  openDocBinaryinUrl:RNFileViewerModule.openDocBinaryinUrl
}
