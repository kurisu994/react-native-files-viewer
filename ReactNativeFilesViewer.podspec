require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "ReactNativeFilesViewer"
  s.version      = package["version"]
  s.summary      = package["name"]
  s.description  = package["description"]
  s.homepage     = "https://github.com/kurisu994/react-native-files-viewer"
  s.license      = "MIT"
  s.author             = { "author" => "Kurisu" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/kurisu994/react-native-files-viewer.git", :tag => s.version  }
  s.source_files  = "ios/**/*.{h,m}"
  s.requires_arc = true
  s.dependency "React"
end

  