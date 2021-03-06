package org.primefaces.examples.moviecollector.view;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.examples.moviecollector.beans.CategoryBean;
import org.primefaces.examples.moviecollector.domain.Category;
import org.primefaces.examples.moviecollector.service.CategoryService;
import org.primefaces.model.DefaultTreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("createCategory")
@Scope("request")
public class CreateCategory implements Serializable {

	private Category category = new Category();
	
	private String test;

	private DefaultTreeNode selectedNode;
	
	private CategoryService categoryService;

	public CreateCategory() {
	}

	@Autowired
	public CreateCategory(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	public void save(ActionEvent actionEvent) {
		FacesContext context = FacesContext.getCurrentInstance();
		CategoryBean bean = (CategoryBean) context.getApplication().evaluateExpressionGet(context, "#{categoryBean}", CategoryBean.class);
		
		if (selectedNode != null) {
			category.setParent((Category) selectedNode.getData());
			new DefaultTreeNode(category, selectedNode).setSelected(false);
			selectedNode.setExpanded(true);
			selectedNode.setSelected(false);
		}
		else {
			new DefaultTreeNode(category, bean.getRoot());	
		}
		
		categoryService.createNew(category);
		FacesMessage facesMessage = new FacesMessage(
				FacesMessage.SEVERITY_INFO, "Info", "Category is saved");
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		category = new Category();
	}

	public void remove(ActionEvent actionEvent) {
		if (selectedNode != null) {
			if (selectedNode.isLeaf()) {
				selectedNode.getParent().getChildren().remove(selectedNode);
				categoryService.remove((Category) selectedNode.getData());	
			}
			else {
				FacesMessage facesMessage = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Error", "Can only remove leaf nodes!");
				FacesContext.getCurrentInstance().addMessage(null, facesMessage);
			}
		}
		else {
			FacesMessage facesMessage = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Error", "Must select a category to remove!");
			FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		}
	}

    public void setCategory(Category category) {
		this.category = category;
	}

	public Category getCategory() {
		return category;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public String getTest() {
		return test;
	}

	public void setSelectedNode(DefaultTreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public DefaultTreeNode getSelectedNode() {
		return selectedNode;
	}

}
