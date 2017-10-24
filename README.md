# Android性能优化之启动速度优化
&emsp;&emsp;Android app 启动速度优化，首先谈谈为什么会走到优化这一步，如果一开始创建 app 项目的时候就把这个启动速度考虑进去，那么肯定就不需要重新再来优化一遍了。这是因为在移动互联网时代，大家都追求快，什么功能都是先做出来再说，其他的可以先不考虑，先占据先机，或者验证是否值得做。那为什么要这么做呢？我个人的观点有以下几点

- 如果 app 不能快速开发出来，先放出去验证一下可行性，可能连是否值得做都不知道，如果花很长时间做了一个对用户无价值的功能，那么还不如不做
- 如果 app 不能快速做出来，可能被竞争对手捕获先机，那么可能错失最佳商业时机
- 如果一开始就规定不能影响启动速度的这个目标，那么做功能的时候就会有束缚，快不起来
- app 初期大家都忙着开发新功能，迭代新版本，没有时间停下来做优化
- 同类型 app 变多，竞争对手变多，大家才开始关注启动性能，才开始做启动速度优化（有主动出击也有被动优化）

## 引起性能问题的原因
&emsp;&emsp;随着项目不断的快速迭代，往往会造成App启动卡慢现象，因为可能在App主进程启动阶段或者在主界面启动阶段放了很多初始化其他业务的逻辑，而这些业务落地可能一开始并不需要用到。本文从作者的亲身经历给大家阐述启动速度优化相关的点点滴滴，为启动速度优化提供一种思路给大家参考。

## 为什么要做启动速度优化
&emsp;&emsp;App启动卡慢会影响一个App的卸载率和使用率。启动速度快会给人一种轻快的感觉，减少用户等待时间。如果一个App从点击桌面图标到看到主界面花了10秒，请问你能接受么？忍耐不好的估计直接就卸载了，或者没等打开就直接Home键按出去，然后杀进程了。这样一来App卸载率提升了，使用率下降了。所以对于有大量用户的App来说，这些性能细节是很重要的，毕竟用户就是钱啊。

## 分析制定优化技术路线 
### 分析启动性能瓶颈
&emsp;&emsp;在具体的优化之前，首先我们得找到需要优化的地方，怎么找？这就要求了解Android App的启动原理，我们要知道一个App从点击桌面图标到我们看到App的主界面整个过程中经过了哪些步骤，哪些地方是我们可以优化的地方。下图是App启动过程的一个大概描述。

![](/docpic/start_up.png "启动流程")

### 制定优化方向
&emsp;&emsp;从上面的分析可以看出，App启动过程中我们优化的地方包括主进程启动流程和主界面启动流程，主进程启动就是Application的创建过程，主界面启动就是MainActivity的创建过程。只需要分别对这两个部分进行优化即可。

1. Application中attachBaseContext最早被调用，随后是onCreate方法，尽量在这两个方法中不要有耗时操作。
1. MainActivity中重点关注onCreate，onResume，onWindowFocusChange，Activity启动完成结束标志这里采用没有使用生命周期函数，而是以主界面View的第一次绘制作为启动完成的标志，View被第一次绘制证明View即将展示出来被我们看到。所以我们在Activity根布局中加入一个自定义View，以它的onDraw方法第一次回调作为Activity启动完成的标志。

		public class FirstDrawListenView extends View {
		    private boolean isFirstDrawFinish = false;
		
		    private IFirstDrawListener mIFirstDrawListener;
		
		    public FirstDrawListenView(Context context, AttributeSet attrs) {
		        super(context, attrs);
		    }
		
		    @Override
		    protected void onDraw(Canvas canvas) {
		        super.onDraw(canvas);
		        if (!isFirstDrawFinish) {
		            isFirstDrawFinish = true;
		            if (mIFirstDrawListener != null) {
		                mIFirstDrawListener.onFirstDrawFinish();
		            }
		        }
		    }
		
		    public void setFirstDrawListener(IFirstDrawListener firstDrawListener) {
		        mIFirstDrawListener = firstDrawListener;
		    }
		
			public interface IFirstDrawListener {
			    void onFirstDrawFinish();
			}
		}



## 怎么统计数据查看优化前后的数据对比
&emsp;&emsp;通过上面的分析，我们可以统计进程启动各个阶段的耗时点，以及Activity启动各个阶段的耗时点（这个步骤需要额外在主布局中加入一个自定义的空View，监听它的onDraw方法的第一次回调），可以通过埋点数据收集这些数据，在优化之前可以先加入埋点数据，统计上报各个时间段的埋点，所以需要先发个版本验证一下优化之前的情况。统计数据的机制加入之后，就可以着手优化了，一边优化一边对比，可以很清楚看到优化前后的对比。

## 制定优化的目标
&emsp;&emsp;由于App启动速度在不同是设备上差别很大，所以目标不太好定，但是做事情总得要有个目标吧。首先我们使用大家都熟悉的一个概念“秒开”，其次是冷启动热启动分开算，再次是分出不同的机型（高端机，中端机型，低端机型），最后是需要先看看没优化之前的启动数据。这样就可以定义出类似下面的目标：

1. 高端机型1秒内打开（比如小米5，Android6.0以上）
1. 中端机型1.5秒内打开
1. 低端机型2.5秒内打开

&emsp;&emsp;上面是终极目标，真正优化的时候，要结合App实际数据以及团队实际情况来定自己的优化目标。

## 优化具体步骤
&emsp;&emsp;一般来说，快速优化最好的方式就是把不必要提前做的操作放到异步线程中去做，也就是我们经常做的异步加载。除了异步加载，一些真正有性能影响的代码需要做具体优化。下面依次介绍一些具体的优化实施步骤。
### 封装一个打印耗时点日志的辅助类
&emsp;&emsp;优化的时候为了快速定位耗时的代码块，我们需要在耗时代码块的前后加上日志，统计耗时具体的时间。这个能在Debug模式下帮助我们快速分析定位到耗时的代码块，然后我们在针对具体的耗时代码块去下手，看看怎么优化。

### 异步加载一：Application中加入异步线程
&emsp;&emsp;在Application中封装两个方法：onSyncLoad（同步加载）和 onAsyncLoad（异步加载，在Thread中执行），把不需要同步加载的部分全部放到onAsyncLoad方法，需要同步的方法放到onSyncLoad中去做，就这种简单的分类就可以带来一个很好的优化效果。

	public class StartUpApplication extends Application {
	
	    @Override
	    public void onCreate() {
	        // 程序创建时调用，次方法应该执行应该尽量快，否则会拖慢整个app的启动速度
	        super.onCreate();
	        onSyncLoadForCreate();
	    }
	
	    @Override
	    protected void attachBaseContext(Context base) {
	        super.attachBaseContext(base);
	        onSyncLoad();
	        onAsyncLoad();
	    }
	
	    private void onSyncLoadForCreate() {
	        AppStartUpTimeLog.isColdStart = true;   // 设置为冷启动标志
	        AppLog.log("StartUpApplication onCreate");
	        AppStartUpTimeLog.logTimeDiff("App onCreate start", false, true);
	        BlockingUtil.simulateBlocking(500); // 模拟阻塞100毫秒
	        AppStartUpTimeLog.logTimeDiff("App onCreate end");
	    }
	
	    private void onSyncLoad() {
	        AppLog.log("StartUpApplication attachBaseContext");
	        AppStartUpTimeLog.markStartTime("App attachBaseContext", true);
	        BlockingUtil.simulateBlocking(200); // 模拟阻塞100毫秒
	        AppStartUpTimeLog.logTimeDiff("App attachBaseContext end", true);
	    }
	
	    public void onAsyncLoad() {
	        new Thread(new Runnable() {
	            @Override
	            public void run() {
	                // 异步加载逻辑
	            }
	        }, "ApplicationAsyncLoad").start();
	    }
	}

### 异步加载二：MainActivity中加入异步线程
&emsp;&emsp;这一步骤与Application的优化思路一样，也是封装onSyncLoad和onAsyncLoad方法对现有代码进行一个分类，但是这两个方法的调用时机要晚一点，是在主界面首屏绘制完成的时候调用。这个步骤也需要new一个Thead，属于额外的开销，不过这不影响我们整体性能。

### 延迟加载功能：首屏绘制完成之后加载
&emsp;&emsp;还有些操作必须要在UI线程做，但是不需要那么快速就做，这里放到首屏绘制完成之后，我们之前在主布局中加入一个空的View来监听它的第一次onDraw回调，我们通过接口的方式把这个事件接到我们的MainActivity中去（Activity中实现接口的onFirstDrawFinish方法）。为了让用户尽快看到主界面，我们就可以把一些需要在UI线程执行，但是又不需要那么快的执行的操作放到onFirstDrawFinish中去。

### 动态加载布局：主布局文件优化
&emsp;&emsp;把主界面中不需要第一次就用到的布局全部使用动态加载的方式来处理，使用ViewStub或者直接在使用时动态addView的方式。

### 主布局文件深度优化
&emsp;&emsp;如果做了上面这些优化还是会发现进入主界面还是有些慢，那么需要重点关注主布局文件了。主布局文件的复杂度直接影响到了Activity的加载速度，这个时候需要对主布局文件进行深度优化了。Activity在加载布局的时候，会对整个布局文件进行解析，测量（measure）,布局（layout）和绘制（draw），所以设计简单合理的布局尤为重要。布局的优化不做详细介绍，网上很多文章的。几个重要的优化如下：

1. 减少布局层级
1. 减少首次加载View的数量
1. 减少过度绘制

&emsp;&emsp;如果需要看看主布局加载具体用了多少时间，需要用自定ViewGroup作为根布局根元素，然后监控它的onInflateFinished，onMeasure，onLayout，onDraw方法，通过我们之前写好的打印时间日志的辅助类，打印一些关键日志，可以分析出具体的耗时的步骤，还可以定位哪个View加载耗时最长。

### 功能代码深度优化
&emsp;&emsp;前面的优化步骤中，我们有部分耗时操作放到了首屏绘制onFirstDrawFinish之后来做了，这里会带来一个体验上的问题，虽然进入主界面变快了，但是可能进入之后短暂的时间类UI线程是阻塞的，如果有其他的UI操作可能会卡主，因为onFirstDrawFinish中挂了很多耗时的操作，需要等这些做完之后UI线程才能空闲。所以我们还需要对一些功能代码进行优化，确保其真正用时少。另外我们异步加载线程中的操作是有一定的安全风险的，如果有些操作很耗时，可能导致我们进入主界面需要用到数据时还没有准备好，所以异步加载我们要注意代码块的顺序，如果有些非常耗时的操作考虑用单独的线程去处理。

## 总结
&emsp;&emsp;优化是一条持续之路，通过优化我们可以了解到影响启动性能的因素有哪些，这样我们平时在编码的过程中就会多注意自己的代码性能。本文从全局的角度去看待整个启动性能优化，看起来好像还挺容易，但是可能实际过程中优化并不会很顺利，不同的设备上可能表现不一样，有时候可能启动一个服务都会耗时。所以要想真正的不耗时，那就是大招：删除它吧。