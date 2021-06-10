
Pod::Spec.new do |s|
  s.name         = "RNFileViewerModule"
  s.version      = "1.0.0"
  s.summary      = "RNFileViewerModule"
  s.description  = <<-DESC
                  RNFileViewerModule
                   DESC
  s.homepage     = "https://github.com/philipphecht/react-native-doc-viewer"
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/philipphecht/react-native-doc-viewer.git", :tag => "master" }
  s.source_files  = "ios/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"

end

  