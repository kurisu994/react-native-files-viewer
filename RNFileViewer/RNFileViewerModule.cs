using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace com.kurisu.fileviewer.RNFileViewerModule
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNFileViewerModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNFileViewerModuleModule"/>.
        /// </summary>
        internal RNFileViewerModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNFileViewerModule";
            }
        }
    }
}
