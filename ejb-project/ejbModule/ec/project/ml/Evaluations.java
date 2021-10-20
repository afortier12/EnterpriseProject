package ec.project.ml;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="Evaluations")
@NamedQueries({
    @NamedQuery(name = "Evaluator.findByName", query = "SELECT a FROM Evaluations a WHERE a.name = :name")
})
public class Evaluations {
	
	@Id
    @GeneratedValue
    private Integer id;
	private int numTrainInstances;
	private int numTestInstances;
	private String name;
	private double correct;
	private double incorrect;
	private double kappa;
	private double MAE;
	private double RMSE;
	private double RAE;
	private double RRSE;
	public Evaluations(int numTrainInstances, int numTestInstances, String name, double correct, double incorrect,
			double kappa, double mAE, double rMSE, double rAE, double rRSE) {
		super();
		this.numTrainInstances = numTrainInstances;
		this.numTestInstances = numTestInstances;
		this.name = name;
		this.correct = correct;
		this.incorrect = incorrect;
		this.kappa = kappa;
		MAE = mAE;
		RMSE = rMSE;
		RAE = rAE;
		RRSE = rRSE;
	}
	public int getNumTrainInstances() {
		return numTrainInstances;
	}
	public void setNumTrainInstances(int numTrainInstances) {
		this.numTrainInstances = numTrainInstances;
	}
	public int getNumTestInstances() {
		return numTestInstances;
	}
	public void setNumTestInstances(int numTestInstances) {
		this.numTestInstances = numTestInstances;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getCorrect() {
		return correct;
	}
	public void setCorrect(double correct) {
		this.correct = correct;
	}
	public double getIncorrect() {
		return incorrect;
	}
	public void setIncorrect(double incorrect) {
		this.incorrect = incorrect;
	}
	public double getKappa() {
		return kappa;
	}
	public void setKappa(double kappa) {
		this.kappa = kappa;
	}
	public double getMAE() {
		return MAE;
	}
	public void setMAE(double mAE) {
		MAE = mAE;
	}
	public double getRMSE() {
		return RMSE;
	}
	public void setRMSE(double rMSE) {
		RMSE = rMSE;
	}
	public double getRAE() {
		return RAE;
	}
	public void setRAE(double rAE) {
		RAE = rAE;
	}
	public double getRRSE() {
		return RRSE;
	}
	public void setRRSE(double rRSE) {
		RRSE = rRSE;
	}
	
	

}
