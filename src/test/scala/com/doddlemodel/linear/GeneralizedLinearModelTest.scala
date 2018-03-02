package com.doddlemodel.linear

import breeze.linalg.{DenseMatrix, DenseVector}
import com.doddlemodel.base.{Predictor, Regressor}
import com.doddlemodel.data.Types.{Features, RealVector, Target}
import org.scalatest.{FlatSpec, Matchers}

class GeneralizedLinearModelTest extends FlatSpec with Matchers {

  private class DummyLinear(val w: Option[RealVector]) extends Regressor with GeneralizedLinearModel[Double] {
    protected def newInstance(w: RealVector): Predictor[Double] = new DummyLinear(Some(w))
    protected[linear] def meanFunction(latent: RealVector): Target[Double] = latent
    protected[linear] def loss(w: RealVector, x: Features, y: Target[Double]): Double = 0
    protected[linear] def lossGrad(w: RealVector, x: Features, y: Target[Double]): RealVector = w
  }

  "Generalized linear model" should "throw exception when using fit, predict on trained, untrained models" in {
    val x = DenseMatrix.rand[Double](10, 5)
    val y = DenseVector.rand[Double](10)
    val model = new DummyLinear(None)

    an [IllegalArgumentException] should be thrownBy model.predict(x)
    val trainedModel = model.fit(x, y)
    an [IllegalArgumentException] should be thrownBy trainedModel.fit(x, y)
  }

  it should "implement predictor functions" in {
    val x = DenseMatrix.rand[Double](10, 5)
    val y = DenseVector.rand[Double](10)
    val model = new DummyLinear(None)

    val trainedModel = model.fit(x, y)
    val yPred = trainedModel.predict(x)
    yPred.length shouldEqual y.length
  }
}
