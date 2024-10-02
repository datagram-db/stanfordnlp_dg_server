# This is a sample Python script.
import os.path
import sys

# Press Maiusc+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.
import requests
import zipfile
import os
import shutil
import platform
import tarfile



def download(url, file):
    if not os.path.exists(file):
        print(f"downloading {url}")
        r = requests.get(url, verify=False,stream=True)
        r.raw.decode_content = True
        with open(file, 'wb') as f:
            shutil.copyfileobj(r.raw, f)

def unzip(file, folder):
    if not os.path.isdir(folder) and not os.path.exists(folder):
        print(f"unzipping {file}")
        with zipfile.ZipFile(file, "r") as zip_ref:
            zip_ref.extractall(folder)

def untargz(file, folder):
    if not os.path.isdir(folder) and not os.path.exists(folder):
        print(f"unzipping {file}")
        # open file
        file = tarfile.open(file)
        # extracting file
        file.extractall(folder)
        file.close()

def download_java():
    if "posix" in os.name:
        if "arm" in str(cpu()).lower():
            download("https://download.oracle.com/java/17/archive/jdk-17.0.11_linux-aarch64_bin.tar.gz", "java.tar.gz")
        else:
            download("https://download.oracle.com/java/17/archive/jdk-17.0.11_linux-x64_bin.tar.gz", "java.tar.gz")
        untargz("java.tar.gz", "java")
    elif "nt" in os.name:
        download("https://download.oracle.com/java/17/archive/jdk-17.0.11_windows-x64_bin.zip", "java.zip")
        unzip("java.zip", "java")
    elif sys.platform == "darwin":
        if "arm" in str(cpu()).lower():
            download("https://download.oracle.com/java/17/archive/jdk-17.0.11_macos-aarch64_bin.tar.gz", "java.tar.gz")
        else:
            download("https://download.oracle.com/java/17/archive/jdk-17.0.11_macos-x64_bin.tar.gz", "java.tar.gz")
        untargz("java.tar.gz", "java")

def osname():
    return os.name

def cpu():
    platform.processor()

def print_hi(name):
    # Use a breakpoint in the code line below to debug your script.
    print(f'Hi, {name}')  # Press Ctrl+F8 to toggle the breakpoint.

import subprocess

def run(ls, myenv):
    res = subprocess.run(ls, env=myenv)
    return res.stdout

def remove(path):
    if os.path.exists(path):
        if os.path.isdir(path):
            shutil.rmtree(path)
        elif os.path.isfile(path):
            os.remove(path)

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    if not os.path.isfile(os.path.join("src","python","StanfordNLPExtractor","resources","StanfordNLPExtractor.jar")):
        download("https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.6.3/apache-maven-3.6.3-bin.zip", "maven363.zip")
        download_java()
        unzip("maven363.zip", "maven363")
        import shutil
        if not os.path.exists("merge_done"):
            shutil.copytree(os.path.join("java", "jdk-17.0.11"), os.path.join("maven363", "apache-maven-3.6.3"), dirs_exist_ok=True)
            with open("merge_done", "w") as f:
                f.write("done")
        import os
        my_env = os.environ.copy()
        print(os.path.abspath(os.path.join("java", "jdk-17.0.11")))
        my_env["JAVA_HOME"] = os.path.abspath(os.path.join("java", "jdk-17.0.11"))
        my_env["M2_HOME"] = os.path.abspath(os.path.join("maven363", "apache-maven-3.6.3"))
        my_env["M2"] = os.path.abspath(os.path.join("maven363", "apache-maven-3.6.3", "bin"))
        my_env["PATH"] = my_env["JAVA_HOME"]+os.pathsep+my_env["M2"]+os.pathsep+os.environ["PATH"]
        os.chmod(os.path.abspath(os.path.join("maven363", "apache-maven-3.6.3", "bin", "mvn")),0o777)
        print(run([os.path.abspath(os.path.join("maven363", "apache-maven-3.6.3", "bin", "mvn")), "-f", os.path.abspath("pom.xml"), "clean", "compile", "assembly:single"], my_env))
        shutil.copyfile(os.path.join("target","StanfordNLPExtractor-1.0-SNAPSHOT-jar-with-dependencies.jar"), os.path.join("src","python","StanfordNLPExtractor", "resources","StanfordNLPExtractor.jar"))
    remove('./java')
    remove('./maven363')
    remove('./java.tar.gz')
    remove('./java.zip')
    remove('./maven363.zip')
    remove('./merge_done')
# SeyCharm help at https://www.jetbrains.com/help/pycharm/
