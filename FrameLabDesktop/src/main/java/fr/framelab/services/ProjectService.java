package fr.framelab.services;

import fr.framelab.dao.ProjectDAO;
import fr.framelab.models.Project;

import java.util.List;

public class ProjectService {
    private final ProjectDAO projectDAO;

    public ProjectService(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public Project getProject(int projectId) {
        return this.projectDAO.findById(projectId);
    }

    public List<Project> getUserProjects(int userId) {
        return this.projectDAO.findByUserId(userId);
    }

    public List<Project> getUserProjectsByChallenge(int challengeId, int userId) {
        return this.projectDAO.findByChallengeIdAndUserId(challengeId, userId);
    }

    public void saveProject(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null");
        }

        if (project.getId() == -1) {
            this.projectDAO.save(project);
        } else {
            this.projectDAO.update(project);
        }
    }

    public void deleteProject(int id) {
        this.projectDAO.deleteById(id);
    }
}
