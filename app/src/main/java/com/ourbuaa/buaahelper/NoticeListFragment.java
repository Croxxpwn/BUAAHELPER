package com.ourbuaa.buaahelper;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

//import com.yanzhenjie.swiperecyclerview.view.ListViewDecoration;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class NoticeListFragment extends Fragment implements BUAA_RecyclerViewAdapter.OnClickListener, BUAA_RecyclerViewAdapter.GarbageCollector {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private BUAA_RecyclerViewAdapter buaa_recyclerViewAdapter;

    private ContentProvider provider, trash;
    private SwipeMenuRecyclerView mSwipeMenuRecyclerView;
    private ItemTouchHelper itemTouchHelper;
    private BUAAItemTouchHelperCallback buaaItemTouchHelperCallback;
    private SQLiteUtils sqLiteUtils;
    private View view;
    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //加载更多调下面这个类
    private RecyclerView.OnScrollListener mOnScrollListener = new ListScrollListener();
    // private MenuAdapter mMenuAdapter;
    /**
     * 刷新监听。
     */
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mSwipeMenuRecyclerView.postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 2000);
        }
    };
    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.item_height);

            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加左侧的，如果不添加，则左侧不会出现菜单。
           /* {
                SwipeMenuItem addItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_green)// 点击的背景。
                        .setImage(R.mipmap.ic_action_add) // 图标。
                        .setWidth(width) // 宽度。
                        .setHeight(height); // 高度。
                swipeLeftMenu.addMenuItem(addItem); // 添加一个按钮到左侧菜单。

                SwipeMenuItem closeItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_close)
                        .setWidth(width)
                        .setHeight(height);

                swipeLeftMenu.addMenuItem(closeItem); // 添加一个按钮到左侧菜单。
            }*/

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
               /* SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_delete)
                        .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);*/
                /*swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。*/

                /*SwipeMenuItem closeItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_purple)
                        .setImage(R.mipmap.ic_action_close)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(closeItem); // 添加一个按钮到右侧菜单。*/

               /* SwipeMenuItem addItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_green)
                        .setText("收藏")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addItem); // 添加一个按钮到右侧菜单。*/
            }
        }
    };
    /**
     * 菜单点击监听。
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        /**
         * Item的菜单被点击的时候调用。
         * @param closeable       closeable. 用来关闭菜单。
         * @param adapterPosition adapterPosition. 这个菜单所在的item在Adapter中position。
         * @param menuPosition    menuPosition. 这个菜单的position。比如你为某个Item创建了2个MenuItem，那么这个position可能是是 0、1，
         * @param direction       如果是左侧菜单，值是：SwipeMenuRecyclerView#LEFT_DIRECTION，如果是右侧菜单，值是：SwipeMenuRecyclerView
         *                        #RIGHT_DIRECTION.
         */
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                // Toast.makeText(mContext, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                // Toast.makeText(mContext, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }

            // TODO 推荐调用Adapter.notifyItemRemoved(position)，也可以Adapter.notifyDataSetChanged();
            // if (menuPosition == 0) {// 删除按钮被点击。
            // sqLiteUtils.StarNotification(adapterPosition);
            //buaa_recyclerViewAdapter.RemoveData(adapterPosition);
            //buaa_recyclerViewAdapter.notifyItemRemoved(adapterPosition);
            //}
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NoticeListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NoticeListFragment newInstance(int columnCount) {
        NoticeListFragment fragment = new NoticeListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public void setSqLiteUtils(SQLiteUtils sqLiteUtils) {
        this.sqLiteUtils = sqLiteUtils;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        mContext = view.getContext();
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView) view.findViewById(R.id.recycler_view);

        mSwipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));// 布局管理器。
        mSwipeMenuRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mSwipeMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        mSwipeMenuRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));// 添加分割线。
        // 添加滚动监听。
        mSwipeMenuRecyclerView.addOnScrollListener(mOnScrollListener);
        ((ListScrollListener) mOnScrollListener).setBuaaContentProvider((BUAAContentProvider) provider);
        // 为SwipeRecyclerView的Item创建菜单
        // 设置菜单创建器。
        mSwipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听。
        mSwipeMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);


        //mMenuAdapter = new MenuAdapter(mDataList);
        //mMenuAdapter.setOnItemClickListener(onItemClickListener);
        ((BUAAContentProvider) provider).setContext(getActivity());

        if (buaa_recyclerViewAdapter == null)
            buaa_recyclerViewAdapter = new BUAA_RecyclerViewAdapter(provider);
        buaa_recyclerViewAdapter.setOnClickListener(this);
        ((BUAAContentProvider) provider).setBuaa_recyclerViewAdapter(buaa_recyclerViewAdapter);
        ((BUAAContentProvider) provider).updateNotifications();
        buaaItemTouchHelperCallback = new BUAAItemTouchHelperCallback(buaa_recyclerViewAdapter);
        buaa_recyclerViewAdapter.setGarbageCollector(this);
        mSwipeMenuRecyclerView.setAdapter(buaa_recyclerViewAdapter);

        //    Toast.makeText(mContext,((Integer)buaa_recyclerViewAdapter.getItemCount()).toString(),Toast.LENGTH_SHORT).show();
        itemTouchHelper = new ItemTouchHelper(buaaItemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mSwipeMenuRecyclerView);
       /* recyclerView = (RecyclerView) view.findViewById(R.id.NoticeList);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        //provider = new ContentProvider();
        ((BUAAContentProvider)provider).setContext(getActivity());

        if (buaa_recyclerViewAdapter == null)
            buaa_recyclerViewAdapter = new BUAA_RecyclerViewAdapter(provider);
        buaa_recyclerViewAdapter.setOnClickListener(this);
        ((BUAAContentProvider) provider).setBuaa_recyclerViewAdapter(buaa_recyclerViewAdapter);
        buaaItemTouchHelperCallback = new BUAAItemTouchHelperCallback(buaa_recyclerViewAdapter);
        buaa_recyclerViewAdapter.setGarbageCollector(this);
        // buaaItemTouchHelperCallback.setMadapter(buaa_recyclerViewAdapter);
        itemTouchHelper = new ItemTouchHelper(buaaItemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(buaa_recyclerViewAdapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        if (onScrollListener == null)
            recyclerView.addOnScrollListener(new DefaultOnScrollListener());
        else
            recyclerView.addOnScrollListener(onScrollListener);
        /*DividerDecoration divider = new DividerDecoration.Builder(getContext(),mLrecyclerviewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.split)
                .build();
        mRecyclerView.addItemDecoration(divider);*/
        // Set the search bar
        MaterialSearchBar materialSearchBar = (MaterialSearchBar) view.findViewById(R.id.searchBar);
        CustomSuggestionsAdapter customSuggestionsAdapter = new CustomSuggestionsAdapter(inflater);
        List<itemForSeachBar> suggestions = new ArrayList<>();
        customSuggestionsAdapter.setSuggestions(suggestions);
        materialSearchBar.setCustomSuggestionAdapter(customSuggestionsAdapter);
        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
            if (buaa_recyclerViewAdapter != null)
                buaa_recyclerViewAdapter.setOnListFragmentInteractionListener(mListener);
        } else {
            // throw new RuntimeException(context.toString()
            //       + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BUAAContentProvider) provider).updateNotifications();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void OnItemClick(CommonItemForList itemForList, int pos) {
        // BUAA_RecyclerViewAdapter.ListItemViewHolder viewHolder = (BUAA_RecyclerViewAdapter.ListItemViewHolder)(recyclerView.getChildViewHolder(view));
        // CommonItemForList itemForList = viewHolder.getItemForList();
        //Bundle bundle = new Bundle();
        // bundle.putInt("ID",itemForList.getId() );
        //bundle.putInt();
        sqLiteUtils.ReadNotificationByID(itemForList.getId());
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("ID", itemForList.getId());
        intent.putExtra("Position", pos);
        // if (itemForList.Detail != null)
        //     intent.putExtra("Detail",itemForList.Detail.toString());
        // intent.putExtra("Adapter",  buaa_recyclerViewAdapter);
        startActivity(intent);
        //Toast.makeText(getActivity(),"Position:"+pos,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnDeleteBtnClick(int pos) {
        //int pos = recyclerView.getChildAdapterPosition(view);
        // buaa_recyclerViewAdapter.RemoveData(pos);
        // buaa_recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnFavoriteBtnClick(int pos) {

    }

    @Override
    public void OnItemClick(int pos) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        //intent.putExtra("ID",itemForList.getId() );
        intent.putExtra("Position", pos);
        // if (itemForList.Detail != null)
        //     intent.putExtra("Detail",itemForList.Detail.toString());
        // intent.putExtra("Adapter",  buaa_recyclerViewAdapter);
        startActivity(intent);
    }

    @Override
    public void OnDataRemoved(CommonItemForList itemForList, int position) {
        //TODO:删除item的接口已经暴露,position表示待删除元素在ITEMS数组中的index，itemForList是待删除元素的详细内容
        //if (trash instanceof BUAAContentProvider) {
        //得到type为TRASH的ContentProvider
        //BUAAContentProvider garbageCollector = (BUAAContentProvider)trash;
        //这是目前默认的删除方式，记得保留这几行
        String type = ((BUAAContentProvider) provider).getType();
        if (!type.equals(BUAAContentProvider.Trash)) {
            sqLiteUtils.HideNotificationByID(itemForList.getId());
        } else
            sqLiteUtils.SeekNotificationByID(itemForList.getId());
        if (provider instanceof BUAAContentProvider) {
            ((BUAAContentProvider) provider).deleteDataInList(position);
        }

        //}
    }

    public void setProvider(ContentProvider provider) {
        this.provider = provider;
    }

   /* private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Toast.makeText(mContext, "我是第" + position + "条。", Toast.LENGTH_SHORT).show();
        }
    };*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(CommonItemForList item);
    }

    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.menu_all_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        } else if (item.getItemId() == R.id.menu_open_rv_menu) {
            mSwipeMenuRecyclerView.smoothOpenRightMenu(0);
        }
        return true;
    }*/
}
