import SwiftUI
import shared

struct ContentView: View {

    @State private var showAlert = false;
    @State private var textShow: String = ""

    var body: some View {
        let dict = [
            "Log Test": {
                KLoger().v(msg: "this is log test")
                return
            },
            "Http Post Test": {
                let url = "https://api.androidhive.info/volley/person_object.json"
                NetHelper.companion.with()
                        .withHeader(header: ["header1": "00000", "header2": "111111"])
                        .jsonPost(url: url, params: ["key": "value"], callback: { (retCode: KotlinInt, header: [String: [String]], body: String) in
                            KLoger().v(msg: body)
                            self.textShow = "retCode: \(retCode), header: \(header), body: \(body)"
                            self.showAlert = true
                        })

            },
            "Http Get Test": {
                let url = "https://suggest.taobao.com/sug?code=utf-8&q=%E8%A1%A3%E6%9C%8D&callback=cb"
                NetHelper.companion.with()
                        .get(url: url, callback: { (retCode: KotlinInt, header: [String: [String]], body: String) in
                            KLoger().v(msg: body)
                            self.textShow = "retCode: \(retCode), header: \(header), body: \(body)"
                            self.showAlert = true
                        })

            },
            "Http Download Test": {
                let url = "https://inews.gtimg.com/om_bt/OI3Uod_dCKpBvNO-lHCcG9rkw6nufFuFJqm1aGTeWs0gAAA/1000"
                AlamfireHelper.companion.with()
                        .iosDownload(url: url, callback: { (retCode: KotlinInt, header: [String: [String]], nsData: Data?) in
                            if nsData != nil {
                                IosKFile.companion
                                        .withDocDir(context: self)
                                        .withSubDir(subDir: "test")
                                        .withFileName(fileName: "test.webp")
                                        .nSData2File(nsdata: nsData!, result: { (path: String?) in
                                            self.textShow = "图片已写入:\(path)，请查看"
                                            self.showAlert = true
                                        })
                            }
                        })

            },
            "MMKV TEST": {
                let testValue = "this is my test string"
                let mmkv = KMMKV(file: "ssss")
                mmkv.putString(key: "delan", value: testValue)

                let getValue = mmkv.getString(key: "delan", defValue: "")
                KLoger().v(msg: "getValue: " + (getValue ?? "nil"))
                if(getValue == testValue) {
                    self.textShow = "mmkv check success, getValue: \(getValue), testValue: \(testValue)"
                } else {
                    self.textShow = "mmkv check success"
                }
                self.showAlert = true
            },
            "File IO TEST": {
                let sb = "this is my 测试\r\n这是换行测试"
                let data = sb.data(using: .utf8)

                IosKFile.companion
                        .withDocDir(context: self)
                        .withSubDir(subDir: "test")
                        .withFileName(fileName: "a.txt")
                        .nSData2File(nsdata: data!, result: { (filePath: String?) -> Void in
                            if filePath != nil {
                                IosKFile.companion
                                        .withAbsolutePath(path: filePath!)
                                        .readFile2NSData(result: { (readData: Data?) -> Void in
                                            if readData != nil {
                                                let docString = String(decoding: readData!, as: UTF8.self)
                                                self.textShow = "Doc文件匹配情况：\(docString == sb), filePath: \(filePath)"
                                                self.showAlert = true
                                            }
                                        })
                            }
                        })
            },
            "异步任务测试": {
                KTask.companion.post(scope: KScopeKt.uiScope, run: {
                    KLoger.shared.i(msg: "task post run")
                })
                KTask.companion.postDelay(scope: KScopeKt.ioScope, delay: 5000, run: {
                    //can't print log in none ui thread in ios
                    //KLoger.shared.i(msg: "task post delay run")
                })
                KTask.companion.postDelay(scope: KScopeKt.uiScope, delay: 7000, run: {
                    KLoger.shared.i(msg: "task post delay run in main thread")
                })
            },
        ]

        List {
            ForEach(dict.keys.sorted(), id: \.self) { key in
                Text("\(key)").onTapGesture(perform: {
                    dict[key]?()
                })
            }
        }.onAppear {}
                .alert(isPresented: $showAlert) {
                    Alert(title: Text("Result"), message: Text(textShow), dismissButton: .default(Text("OK")))
                }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}