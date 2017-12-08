package com.janusresearch.genoaModelTool.dom;

import com.intellij.debugger.requests.Requestor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.janusresearch.genoaModelTool.genoa.impl.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.bind.*;
import java.io.*;

import com.janusresearch.genoaModelTool.xsd.Behavior;
import com.janusresearch.genoaModelTool.xsd.Model;
import com.janusresearch.genoaModelTool.xsd.ObjectFactory;
import icons.*;

@SuppressWarnings({"ConstantConditions"})
public class GenoaModel {
    private final Project project;
    private JTree myTree;
    private final VirtualFile myFile;
    private final String fileName;
    private final String filePath;

    public GenoaModel(Project project, VirtualFile file) {
        myFile = file;
        this.project = project;
        filePath = file.getPath().substring(file.getPath().indexOf(project.getName()));
        fileName = file.getNameWithoutExtension();

        //Create DomManager, FileDescription and register the description
//        DomManager manager = DomManager.getDomManager(project);

        //Get PsiFile from VirtualFile and cast that to XmlFile
//        XmlFile xmlFile = (XmlFile) PsiManager.getInstance(project).findFile(file);
        try {
            JAXBContext jc = JAXBContext.newInstance("com.janusresearch.genoaModelTool.xsd", ObjectFactory.class.getClassLoader());
            Unmarshaller um = jc.createUnmarshaller();
            InputStream is = file.getInputStream();
            JAXBElement<?> modelElement = (JAXBElement<?>) um.unmarshal(is);
            Model model = (Model) modelElement.getValue();
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            OutputStreamWriter osw = new OutputStreamWriter(file.getOutputStream(null));
            m.marshal(model, osw);
            osw.close();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        //Get the GenoaRoot File Element
//        XmlTag genoaRoot = manager.getFileElement(xmlFile, GenoaRoot.class).getRootElement().getXmlTag();
//        buildTree(genoaRoot);
    }

    private void buildTree(XmlTag rootTag){
//        this.model = new ModelImpl(project, rootTag);
//        GenoaObject myModel = new ModelImpl(project, rootTag);
        /*if (test instanceof ModelImpl) {
            Debug.print(((ModelImpl) test).getName());
        }*/
//        DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode(myModel);
//        DefaultTreeModel myTreeModel = new DefaultTreeModel(rootTreeNode);
//        myTree = new Tree(myTreeModel);

//        CustomRenderer customRenderer = new CustomRenderer();

//        addChildren(myModel, rootTreeNode, rootTag);
//        myTree.setCellRenderer(customRenderer);
    }

    private void addChildren(GenoaObject parentObj, DefaultMutableTreeNode parentTreeNode, XmlTag parentXmlTag) {
        XmlTag[] childTags = parentXmlTag.getSubTags();
        DefaultMutableTreeNode childTreeNode = null;
        GenoaObject currentObj;
        /*for (XmlTag t : childTags) {
            switch(t.getName()) {
                case GenoaXmlTags.NAME :
                case GenoaXmlTags.COMMENT :
                case GenoaXmlTags.DESCRIPTION :
                case GenoaXmlTags.DESTINATION :
                case GenoaXmlTags.ENTITY_NAME :
                case GenoaXmlTags.SUPER_ENTITY_NAME :
                case GenoaXmlTags.VAR_NAME :
                    break;
                case GenoaXmlTags.ENTITY :
                    currentObj = ((ModelImpl) parentObj).addEntity(t);
                    childTreeNode = new DefaultMutableTreeNode(currentObj);
                    parentTreeNode.add(childTreeNode);
                    addChildren(currentObj, childTreeNode, t);
                    break;
                case GenoaXmlTags.ATTRIBUTE :
                    currentObj = ((EntityImpl) parentObj).addAttribute(t);
                    childTreeNode = new DefaultMutableTreeNode(currentObj);
                    parentTreeNode.add(childTreeNode);
                    addChildren(currentObj, childTreeNode, t);
                    break;
                case GenoaXmlTags.CHOICES :
                    for (XmlTag c : t.getSubTags()) {
                        currentObj = ((AttributeImpl) parentObj).addChoice(c);
                        DefaultMutableTreeNode choice = new DefaultMutableTreeNode(currentObj);
                        parentTreeNode.add(choice);
                    }
                    break;
                case GenoaXmlTags.TO_ONE :
                    currentObj = ((EntityImpl) parentObj).addToOne(t);
                    childTreeNode = new DefaultMutableTreeNode(currentObj);
                    parentTreeNode.add(childTreeNode);
                    addChildren(currentObj, childTreeNode, t);
                    break;
                case GenoaXmlTags.TO_MANY :
                    currentObj = ((EntityImpl) parentObj).addToMany(t);
                    childTreeNode = new DefaultMutableTreeNode(currentObj);
                    parentTreeNode.add(childTreeNode);
                    addChildren(currentObj, childTreeNode, t);
                    break;
                case GenoaXmlTags.TO_MANY_MAP :
                    currentObj = ((EntityImpl) parentObj).addToManyMap(t);
                    childTreeNode = new DefaultMutableTreeNode(currentObj);
                    parentTreeNode.add(childTreeNode);
                    addChildren(currentObj, childTreeNode, t);
                    break;
                case GenoaXmlTags.TO_MANY_LIST :
                    currentObj = ((EntityImpl) parentObj).addToManyList(t);
                    childTreeNode = new DefaultMutableTreeNode(currentObj);
                    parentTreeNode.add(childTreeNode);
                    addChildren(currentObj, childTreeNode, t);
                    break;
                case GenoaXmlTags.ALIAS :
                    currentObj = ((EntityImpl) parentObj).addAlias(t);
                    childTreeNode = new DefaultMutableTreeNode(currentObj);
                    parentTreeNode.add(childTreeNode);
                    addChildren(currentObj, childTreeNode, t);
                    break;
                case GenoaXmlTags.PROTOTYPE :
                    currentObj = ((ModelImpl) parentObj).addPrototype(t);
                    childTreeNode = new DefaultMutableTreeNode(currentObj);
                    parentTreeNode.add(childTreeNode);
                    addChildren(currentObj, childTreeNode, t);
                    break;
                case GenoaXmlTags.ATTRIBUTE_SETTING :
                    currentObj = ((PrototypeImpl) parentObj).addAttributeSetting(t);
                    childTreeNode = new DefaultMutableTreeNode(currentObj);
                    parentTreeNode.add(childTreeNode);
                    addChildren(currentObj, childTreeNode, t);
                    break;
                case GenoaXmlTags.PROPERTY_PATH_SETTING :
                    currentObj = ((PrototypeImpl) parentObj).addPropertyPathSetting(t);
                    childTreeNode = new DefaultMutableTreeNode(currentObj);
                    parentTreeNode.add(childTreeNode);
                    addChildren(currentObj, childTreeNode, t);
                    break;
                case GenoaXmlTags.RELATIONSHIP_SETTING :
                    currentObj = ((PrototypeImpl) parentObj).addRelationshipSetting(t);
                    childTreeNode = new DefaultMutableTreeNode(currentObj);
                    parentTreeNode.add(childTreeNode);
                    addChildren(currentObj, childTreeNode, t);
                    break;
                case GenoaXmlTags.TO_MANY_LIST_SETTING :
                    currentObj = ((PrototypeImpl) parentObj).addToManyListSetting(t);
                    childTreeNode = new DefaultMutableTreeNode(currentObj);
                    parentTreeNode.add(childTreeNode);
                    addChildren(currentObj, childTreeNode, t);
                    break;
                case GenoaXmlTags.TO_MANY_SETTING :
                    currentObj = ((PrototypeImpl) parentObj).addToManySetting(t);
                    childTreeNode = new DefaultMutableTreeNode(currentObj);
                    parentTreeNode.add(childTreeNode);
                    addChildren(currentObj, childTreeNode, t);
                    break;
                case GenoaXmlTags.VALUE :
                    currentObj = ((PrototypeImpl) parentObj).addValue(t);
                    childTreeNode = new DefaultMutableTreeNode(currentObj);
                    parentTreeNode.add(childTreeNode);
                    addChildren(currentObj, childTreeNode, t);
                    break;
                case GenoaXmlTags.SOURCE_PROPERTY_PATH :
                    currentObj = ((PrototypeImpl) parentObj).addSourcePropertyPath(t);
                    childTreeNode = new DefaultMutableTreeNode(currentObj);
                    parentTreeNode.add(childTreeNode);
                    addChildren(currentObj, childTreeNode, t);
                    break;
                case GenoaXmlTags.PROTOTYPE_REF :
                    currentObj = ((PrototypeImpl) parentObj).addSourcePropertyPath(t);
                    childTreeNode = new DefaultMutableTreeNode(currentObj);
                    parentTreeNode.add(childTreeNode);
                    addChildren(currentObj, childTreeNode, t);
                    break;
                case "literal" :
                case "variableRef1" :
                    childTreeNode = new DefaultMutableTreeNode(t);
                    parentTreeNode.add(childTreeNode);
                    break;
                case "propertyPath" :
                    String parent = t.getParentTag().getName();
                    switch(parent) {
                        case "clause" :
                        case "setProperty" :
                            childTreeNode = new DefaultMutableTreeNode(t);
                            parentTreeNode.add(childTreeNode);
                    }
                    break;
                case "prototypeRef" :
                    if (Objects.equals(t.getParentTag().getName(), "relationshipSetting")) {
                        String[] split = t.getValue().getText().split(",");
                        for (String s : split) {
                            childTreeNode = new DefaultMutableTreeNode(s);
                        }
                    }
                    else {
                        childTreeNode = new DefaultMutableTreeNode(t);
                    }
                    parentTreeNode.add(childTreeNode);
                    break;
                default :
                    childTreeNode = new DefaultMutableTreeNode(t);
                    parentTreeNode.add(childTreeNode);
                    break;
            }
        }*/
    }



    public JTree getMyTree() {
        return myTree;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public VirtualFile getMyFile() {
        return myFile;
    }
}
