#!/bin/sh
echo "Enter Full Java Path: "
read javpath
echo "#!/bin/sh" > ../dist/exe
echo "sudo su << EOF" >> ../dist/exe
echo "export PATH=$javpath:\$PATH" >> ../dist/exe
echo "java -jar jNeelSer.jar" >> ../dist/exe
echo "EOF" >> ../dist/exe
echo "sudo chown \$USER *.*" >> ../dist/exe
chmod +x ../dist/exe

