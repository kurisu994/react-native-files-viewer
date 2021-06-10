
import { NativeModules } from 'react-native';
const RNFileViewerModule = NativeModules.RNFileViewerModule;
export default {
  openDoc: RNFileViewerModule.openDoc,
  openDocb64: RNFileViewerModule.openDocb64,
  playMovie: RNFileViewerModule.playMovie,
  openDocBinaryinUrl:RNFileViewerModule.openDocBinaryinUrl,
  testModule: RNFileViewerModule.testModule
}
